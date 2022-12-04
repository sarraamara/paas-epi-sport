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
import org.springframework.web.client.RestTemplate;

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

    private boolean checkUserSubscription(String userId) {
        final String uri = "http://localhost:8080/users/get-user/" + userId;

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);
        System.out.println(result);
        logger.info("Checking user " + userId + " subscription");
        assert result != null;
        return !result.isEmpty();
    }

    /** TO DO **/
    private boolean checkEmergency(UserHeartRate userHeartRate){
        logger.info("Checking emergency:" + userHeartRate);
        int age = 23;
        final String uri = "http://localhost:8080/users/get-user/" + userHeartRate.getUserId();
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);
        int maxHR = (int) (211 - 0.64*age);
        if(userHeartRate.getHeartRate() > maxHR){
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

    /** TO DO**/
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
