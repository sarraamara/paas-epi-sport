package com.sport.emergencynotifagent.service;

import com.sport.emergencynotifagent.model.CoachProfile;
import com.sport.emergencynotifagent.model.UserCoach;
import com.sport.emergencynotifagent.model.UserCoachHeartRate;
import com.sport.emergencynotifagent.model.UserHeartRate;

import com.sport.emergencynotifagent.repository.UserCoachRepository;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListOffsetsResult;
import org.apache.kafka.clients.admin.OffsetSpec;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class EmergencyNotificationService {

    @Autowired
    private UserCoachRepository userCoachRepository;
    @Autowired
    private RedisTemplate redisTemplate;
    private static final Logger logger = LoggerFactory.getLogger(EmergencyNotificationService.class);

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "coach*-topic", groupId = "ncc")
    public void listenCoach(ConsumerRecord<String, Object> record) {
        // Traitement du message reçu
        System.out.println("topic " + record.topic() + " = " + record.value());
    }

    private boolean verifyCoachSessionInCache(CoachProfile coach) {
        return redisTemplate.hasKey("coach:"+String.valueOf(coach.getCoachId()));
    }

    public void sendMessage(String topic, Object payload) {
        kafkaTemplate.send(topic, payload);
    }

    private List<UserCoach> lookForRelatedCoaches(String userId) {
        logger.info("Look for related coaches to user " + userId + " in database.");

        return userCoachRepository.findUserCoachesByUserId(Integer.parseInt(userId));
    }

    private void sendToNotifChannelQueue(UserCoachHeartRate notifContent) {
        logger.info("Sending to notif channel queue :" + notifContent.toString());
        sendMessage("notif-topic", notifContent);
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
                    sendToNotifChannelQueue(userCoachHeartRate);
                }
                else{
                    logger.info("No authenticated coach in cache session.");
                }
            }
        }
    }

}
