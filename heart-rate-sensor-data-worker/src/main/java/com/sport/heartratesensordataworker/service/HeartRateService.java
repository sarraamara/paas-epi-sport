package com.sport.heartratesensordataworker.service;

import com.google.gson.Gson;
import com.sport.heartratesensordataworker.model.Emergency;
import com.sport.heartratesensordataworker.model.User;
import com.sport.heartratesensordataworker.model.UserHeartRate;
import com.sport.heartratesensordataworker.repository.EmergencyRepository;
import com.sport.heartratesensordataworker.repository.HeartRateRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;

@Service
public class HeartRateService {

    private final HeartRateRepository heartRateRepository;

    private final EmergencyRepository emergencyRepository;

    private static final Logger logger = LoggerFactory.getLogger(HeartRateService.class);
    private final Gson g = new Gson();

    @Value("${user.api.url}")
    String USER_API_URL;

    @Autowired
    public HeartRateService(HeartRateRepository heartRateRepository, KafkaTemplate<String, Object> kafkaTemplate, EmergencyRepository emergencyRepository) {
        this.heartRateRepository = heartRateRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.emergencyRepository = emergencyRepository;
    }

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    @KafkaListener(topics = "hrdata-topic", groupId = "ncc")
    public void listen(ConsumerRecord<String, Object> record) {
        logger.info("GETTING NEW MESSAGE");
        // Traitement du message reçu
        logger.info(record.value().toString());
        UserHeartRate userHeartRate = g.fromJson(record.value().toString(), UserHeartRate.class);
        User current_user = getUser(Integer.parseInt(userHeartRate.getUserId()));
        if (current_user == null)
            logger.info("The user isn't registered");
        else {
            logger.info("Getting userHeartRate:" + userHeartRate);
        }
        if (checkEmergency(userHeartRate, current_user.getAge()) > 0) {
            sendEmergency(userHeartRate);
            saveEmergency(userHeartRate, checkEmergency(userHeartRate, current_user.getAge()));
        }
        saveHeartRateData(userHeartRate);
    }

    private int checkEmergency(UserHeartRate userHeartRate, int age){
        logger.info("Checking emergency:" + userHeartRate);
        int maxHR = (int) (211 - 0.64*age);
        return maxHR-userHeartRate.getHeartRate();

    }

    private void sendEmergency(UserHeartRate userHeartRate) {
        logger.info("WARNING: Sending emergency " + userHeartRate);
        kafkaTemplate.send("emergency-topic", userHeartRate);
    }

    private void saveEmergency(UserHeartRate userHeartRate, int checkEmergency) {
        logger.info("Saving userHeartRate Emergency Data:" + userHeartRate);
        Emergency emergency = new Emergency(
                userHeartRate.getId(),
                userHeartRate.getUserId(),
                userHeartRate.getHeartRate(),
                checkEmergency+userHeartRate.getHeartRate(),
                new Timestamp(System.currentTimeMillis()));
        emergencyRepository.save(emergency);
    }

    private void saveHeartRateData(UserHeartRate userHeartRate){
        logger.info("Saving userHeartRate Data:" + userHeartRate);
        heartRateRepository.save(userHeartRate);
    }

    private User getUser(int userId) {
        final String uri = USER_API_URL + userId;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<User> result = restTemplate.getForEntity(uri, User.class);

        return result.getBody();
    }

}
