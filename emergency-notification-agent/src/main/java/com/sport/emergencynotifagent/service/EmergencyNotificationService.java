package com.sport.emergencynotifagent.service;

import com.sport.emergencynotifagent.model.CoachProfile;
import com.sport.emergencynotifagent.model.UserHeartRate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmergencyNotificationService {

    private RabbitTemplate rabbitTemplate;


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
        return null;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue_emergency}")
    public void receivedMessage(UserHeartRate userHeartRate) {
        logger.info("Received emergency related to user :" + userHeartRate);

        List<CoachProfile> coaches = lookForRelatedCoaches(userHeartRate.getUserId());
    }

}
