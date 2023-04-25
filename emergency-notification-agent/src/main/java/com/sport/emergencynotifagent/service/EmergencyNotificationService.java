package com.sport.emergencynotifagent.service;

import com.sport.common.model.*;
import com.sport.emergencynotifagent.model.UserCoachDto;
import com.sport.emergencynotifagent.model.CoachProfileDto;

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

    private boolean verifyCoachSessionInCache(CoachProfileDto coach) {
        return redisTemplate.hasKey("coach:"+String.valueOf(coach.getCoachId()));
    }

    private List<UserCoachDto> lookForRelatedCoaches(String userId) {
        logger.info("Look for related coaches to user " + userId + " in database.");
        return userCoachRepository.findUserCoachesByUserId(Integer.parseInt(userId));
    }

    private void sendToCoachQueue(UserCoachHeartRate notifContent) {
        logger.info("Sending to coach "+notifContent.getUserCoach().getUserCoachId() +" queue :" + notifContent.toString());
        kafkaTemplate.send("coach"+notifContent.getUserCoach().getUserCoachId()+"-topic", notifContent);
    }

    @KafkaListener(topics = "emergency-topic", groupId = "ncc", containerFactory = "userHeartRateListener")
    public void receivedMessage(UserHeartRate userHeartRate) {
        logger.info("Received emergency related to user :" + userHeartRate);

        List<UserCoachDto> relatedCoaches = lookForRelatedCoaches(userHeartRate.getUserId());

        if (relatedCoaches.size() != 0) {
            for (UserCoachDto coach : relatedCoaches) {
                if (verifyCoachSessionInCache(coach.getCoachProfile())) {
                    logger.info("Coach "+coach.getCoachProfile().getFirstname()+" "+coach.getCoachProfile().getLastname()+" is authenticated.");
                    UserProfile userProfile = new UserProfile(coach.getUserProfile().getUserId(),
                            coach.getUserProfile().getLastname(),
                            coach.getUserProfile().getFirstname(),
                            coach.getUserProfile().getAge(),
                            coach.getUserProfile().getWeight(),
                            coach.getUserProfile().getHeight()
                            );
                    CoachProfile coachProfile = new CoachProfile(
                            coach.getCoachProfile().getCoachId(),
                            coach.getCoachProfile().getLastname(),
                            coach.getCoachProfile().getFirstname()
                    );
                    UserCoach userCoach = new UserCoach(coach.getUserCoachId(),userProfile,coachProfile);
                    UserCoachHeartRate userCoachHeartRate = new UserCoachHeartRate(userCoach, userHeartRate.getHeartRate());
                    sendToCoachQueue(userCoachHeartRate);
                }
                else{
                    logger.info("No authenticated coach in cache session.");
                }
            }
        }
    }
}
