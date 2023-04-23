package com.sport.emergencynotifagent.service;

import com.sport.common.model.UserHeartRate;
import com.sport.common.model.CoachProfile;
import com.sport.common.model.UserCoach;
import com.sport.common.model.UserCoachHeartRate;

import com.sport.emergencynotifagent.repository.UserCoachRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmergencyNotificationService {

    @Autowired
    private UserCoachRepository userCoachRepository;
    @Autowired
    private RedisTemplate redisTemplate;
    private static final Logger logger = LoggerFactory.getLogger(EmergencyNotificationService.class);

    @Autowired
    private KafkaTemplate kafkaTemplate;

    private boolean verifyCoachSessionInCache(CoachProfile coach) {
        return redisTemplate.hasKey("coach:"+String.valueOf(coach.getCoachId()));
    }

    private List<UserCoach> lookForRelatedCoaches(String userId) {
        logger.info("Look for related coaches to user " + userId + " in database.");
        return userCoachRepository.findUserCoachesByUserId(Integer.parseInt(userId));
    }

    private void sendToCoachQueue(UserCoachHeartRate notifContent) {
        logger.info("Sending to coach "+notifContent.getUserCoach().getUserCoachId() +" queue :" + notifContent.toString());
        kafkaTemplate.send("coach"+notifContent.getUserCoach().getUserCoachId()+"-topic", notifContent);
    }

    @KafkaListener(topics = "emergency-topic", groupId = "ncc")
    public void receivedMessage(UserHeartRate userHeartRate) {
        logger.info("Received emergency related to user :" + userHeartRate);

        List<UserCoach> relatedCoaches = lookForRelatedCoaches(userHeartRate.getUserId());

        if (relatedCoaches.size() != 0) {
            for (UserCoach coach : relatedCoaches) {
                if (verifyCoachSessionInCache(coach.getCoachProfile())) {
                    logger.info("Coach "+coach.getCoachProfile().getFirstname()+" "+coach.getCoachProfile().getLastname()+" is authenticated.");
                    UserCoachHeartRate userCoachHeartRate = new UserCoachHeartRate(coach, userHeartRate.getHeartRate());
                    sendToCoachQueue(userCoachHeartRate);
                }
                else{
                    logger.info("No authenticated coach in cache session.");
                }
            }
        }
    }

}
