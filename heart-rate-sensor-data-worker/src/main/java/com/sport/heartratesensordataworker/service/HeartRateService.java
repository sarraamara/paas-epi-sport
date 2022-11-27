package com.sport.heartratesensordataworker.service;

import com.sport.heartratesensordataworker.model.UserHeartRate;
import com.sport.heartratesensordataworker.repository.HeartRateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HeartRateService {

    private final HeartRateRepository heartRateRepository;

    private RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.routingkey_emergency}")
    private String routingkey;

    private static final Logger logger = LoggerFactory.getLogger(HeartRateService.class);

    @Autowired
    public HeartRateService(HeartRateRepository heartRateRepository, RabbitTemplate rabbitTemplate) {
        this.heartRateRepository = heartRateRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    /***TO DO***/
    private boolean checkUserSubscription(String userId) {
        /** SEND REQUEST TO UserProfileService**/
        logger.info("Checking user " + userId + " subscription");
        return true;
    }

    /***TO DO***/
    private boolean checkEmergency(UserHeartRate userHeartRate){
        logger.info("Checking emergency:" + userHeartRate);
        if(userHeartRate.getHeartRate() > 220){
            return true;
        }
        else {
            return false;
        }

    }

    private void sendEmergency(UserHeartRate userHeartRate) {
        logger.info("WARNING: Sending emergency " + userHeartRate);
        rabbitTemplate.convertAndSend(exchange,routingkey, userHeartRate);
    }

    private void saveEmergency(UserHeartRate userHeartRate) {
        logger.info("Saving userHeartRate Emergency Data:" + userHeartRate);

    }

    private void saveHeartRateData(UserHeartRate userHeartRate){
        logger.info("Saving userHeartRate Data:" + userHeartRate);
        heartRateRepository.save(userHeartRate);
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue_hr}")
    public void receivedMessage(UserHeartRate userHeartRate) {

        logger.info("Getting userHeartRate:" + userHeartRate);

        if(!checkUserSubscription(userHeartRate.getUserId())){
            logger.info("User " + userHeartRate.getUserId()+" not subscribed.");
            return;
        }

        if(checkEmergency(userHeartRate)){
            sendEmergency(userHeartRate);
            saveEmergency(userHeartRate);
        }

        saveHeartRateData(userHeartRate);
    }

}