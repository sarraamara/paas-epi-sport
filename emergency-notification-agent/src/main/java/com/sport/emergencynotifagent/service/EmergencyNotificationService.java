package com.sport.emergencynotifagent.service;

import com.sport.emergencynotifagent.model.CoachProfile;
import com.sport.emergencynotifagent.model.UserCoach;
import com.sport.emergencynotifagent.model.UserCoachHeartRate;
import com.sport.emergencynotifagent.model.UserHeartRate;

import com.sport.emergencynotifagent.repository.UserCoachRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmergencyNotificationService {

    @Autowired
    private UserCoachRepository userCoachRepository;

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    private List<CoachProfile> presentCoaches;

    private static final Logger logger = LoggerFactory.getLogger(EmergencyNotificationService.class);

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    @KafkaListener(topics = "emergency-topic", groupId = "ncc")
    public void listenEmergencies(ConsumerRecord<String, Object> record) {
        // Traitement du message reçu
        System.out.println(record.value());
    }

    @KafkaListener(topics = "coach*-topic", groupId = "ncc")
    public void listenCoach1(ConsumerRecord<String, Object> record) {
        // Traitement du message reçu
        System.out.println("topic " + record.topic() + " = " + record.value());
        CoachProfile coach = new CoachProfile(userCoachRepository.findUserCoachesByUserId(Integer.parseInt(record.topic().substring(6))));
        if (record.value().toString().contains("present")) {
            presentCoaches.add(new CoachProfile(Integer.parseInt(record.topic().substring(6))));
        } else {
            presentCoaches.remove(new CoachProfile(Integer.parseInt(record.topic().substring(6))));
        }
    }

    public void sendMessage(String topic, Object payload) {
        kafkaTemplate.send(topic, payload);
    }

    private List<UserCoach> lookForRelatedCoaches(String userId) {
        logger.info("Look for related coaches to user " + userId + " in database.");

        return userCoachRepository.findUserCoachesByUserId(Integer.parseInt(userId));
    }
    private boolean verifyCoachSessionInCache(CoachProfile coach) {
        return presentCoaches.contains(coach);
    }

    private void sendToNotifChannelQueue(UserCoachHeartRate notifContent) {
        logger.info("Sending to notif channel queue :" +notifContent.toString());
        sendMessage("notif-topic", notifContent);

    }

    @KafkaListener(topics = "hrdata-topic", groupId = "ncc")
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
