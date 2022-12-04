package com.sport.heartratesensordataworker.service;

import com.sport.heartratesensordataworker.model.Emergency;
import com.sport.heartratesensordataworker.model.User;
import com.sport.heartratesensordataworker.model.UserHeartRate;
import com.sport.heartratesensordataworker.repository.EmergencyRepository;
import com.sport.heartratesensordataworker.repository.HeartRateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;

@Service
@RequestMapping("/emergency")
@RestController
public class HeartRateService {

    private final HeartRateRepository heartRateRepository;

    private RabbitTemplate rabbitTemplate;

    private final EmergencyRepository emergencyRepository;

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.routingkey_emergency}")
    private String routingkey;

    private static final Logger logger = LoggerFactory.getLogger(HeartRateService.class);

    @Autowired
    public HeartRateService(HeartRateRepository heartRateRepository, RabbitTemplate rabbitTemplate, EmergencyRepository emergencyRepository) {
        this.heartRateRepository = heartRateRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.emergencyRepository = emergencyRepository;
    }

//    private boolean checkUserSubscription(String userId) {
//
//        logger.info("Checking user " + userId + " subscription");
//        assert result != null;
//        return !result.isEmpty();
//    }

    /** TO DO **/
    private int checkEmergency(UserHeartRate userHeartRate, int age){
        logger.info("Checking emergency:" + userHeartRate);
        int maxHR = (int) (211 - 0.64*age);
        return maxHR-userHeartRate.getHeartRate();

    }

    private void sendEmergency(UserHeartRate userHeartRate) {
        logger.info("WARNING: Sending emergency " + userHeartRate);
        rabbitTemplate.convertAndSend(exchange,routingkey, userHeartRate);
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

    @RabbitListener(queues = "${spring.rabbitmq.queue_hr}")
    @GetMapping("/{userId}")
    public void receivedMessage(UserHeartRate userHeartRate) {
        User current_user = getUser(Integer.parseInt(userHeartRate.getUserId()));
        logger.info("Getting userHeartRate:" + userHeartRate);

        int checkEmergency = checkEmergency(userHeartRate,current_user.getAge());
        if(checkEmergency<=0) {
            sendEmergency(userHeartRate);
            saveEmergency(userHeartRate,checkEmergency);
        }

        saveHeartRateData(userHeartRate);
    }

    private User getUser(int userId) {
        final String uri = "http://172.31.253.235:8088/users/get-user/" + userId;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<User> result = restTemplate.getForEntity(uri, User.class);

        return result.getBody();
    }

}
