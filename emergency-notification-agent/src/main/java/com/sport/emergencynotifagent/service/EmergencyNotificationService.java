package com.sport.emergencynotifagent.service;

import com.sport.emergencynotifagent.model.CoachProfile;
import com.sport.emergencynotifagent.model.UserCoach;
import com.sport.emergencynotifagent.model.UserHeartRate;

import com.sport.emergencynotifagent.repository.UserCoachRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmergencyNotificationService {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserCoachRepository userCoachRepository;

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.routingkey_emergency}")
    private String routingkey;

    private static final Logger logger = LoggerFactory.getLogger(EmergencyNotificationService.class);

    private List<UserCoach> lookForRelatedCoaches(String userId) {
        logger.info("Look for related coaches to user " + userId + " in database.");

        return userCoachRepository.findUserCoachesByUserId(Integer.parseInt(userId));
    }
    /** TO DO**/
    private boolean verifyCoachSessionInCache(CoachProfile coach) {
        return redisTemplate.hasKey(String.valueOf(coach.getCoachId()));
    }

    /** TO DO**/
    private void sendToNotifChannelQueue(UserCoach coach) {
    }
    @RabbitListener(queues = "${spring.rabbitmq.queue_emergency}")
    public void receivedMessage(UserHeartRate userHeartRate) {
        logger.info("Received emergency related to user :" + userHeartRate);

        List<UserCoach> relatedCoaches = lookForRelatedCoaches(userHeartRate.getUserId());

        if (relatedCoaches.size() != 0) {
            for (UserCoach coach : relatedCoaches) {
                if (verifyCoachSessionInCache(coach.getCoachProfile())) {
                    logger.info("Coach "+coach.getCoachProfile().getFirstname()+" "+coach.getCoachProfile().getLastname()+" is authenticated.");
                    sendToNotifChannelQueue(coach);
                }
            }
        }
    }

}
