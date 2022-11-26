package com.sport.emergencynotifagent.service;

import com.sport.emergencynotifagent.model.CoachProfile;
import com.sport.emergencynotifagent.model.UserCoach;
import com.sport.emergencynotifagent.model.UserHeartRate;

import com.sport.emergencynotifagent.repository.CoachProfileRepository;
import com.sport.emergencynotifagent.repository.UserCoachRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmergencyNotificationService {

    private RabbitTemplate rabbitTemplate;

    @Autowired
    private UserCoachRepository userCoachRepository;
    @Autowired
    private CoachProfileRepository coachProfileRepository;

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.routingkey_emergency}")
    private String routingkey;

    private static final Logger logger = LoggerFactory.getLogger(EmergencyNotificationService.class);


    @Autowired
    public EmergencyNotificationService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    private List<CoachProfile> lookForRelatedCoaches(String userId) {
        logger.info("Look for related coaches to user " + userId + " in database.");

        List<CoachProfile> relatedCoaches = new ArrayList<>();
        CoachProfile coachProfile = null;

        List<UserCoach> userCoachList = userCoachRepository.findUserCoachesByUserId(Integer.parseInt(userId));
        for(UserCoach userCoach : userCoachList){
            coachProfile = coachProfileRepository.findByCoachId(userCoach.getCoachId());
            logger.info("Related coach(es): "+coachProfile.toString());
            relatedCoaches.add(coachProfile);
        }

        return relatedCoaches;
    }
    /** TO DO**/
    private boolean verifyCoachSessionInCache(CoachProfile coach) {
        return true;
    }

    /** TO DO**/
    private void notifyCoach(CoachProfile coach) {
    }
    @RabbitListener(queues = "${spring.rabbitmq.queue_emergency}")
    public void receivedMessage(UserHeartRate userHeartRate) {
        logger.info("Received emergency related to user :" + userHeartRate);

        List<CoachProfile> relatedCoaches = lookForRelatedCoaches(userHeartRate.getUserId());

        if (relatedCoaches.size() != 0) {
            for (CoachProfile coach : relatedCoaches) {
                if (verifyCoachSessionInCache(coach)) {
                    notifyCoach(coach);
                }
            }
        }
    }

}
