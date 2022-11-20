package com.sport.heartratesensordataworker.service;

import com.sport.heartratesensordataworker.model.UserHeartRate;
import com.sport.heartratesensordataworker.repository.HeartRateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HeartRateService {

    private final HeartRateRepository heartRateRepository;

    private static final Logger logger = LoggerFactory.getLogger(HeartRateService.class);


    @Autowired
    public HeartRateService(HeartRateRepository heartRateRepository) {
        this.heartRateRepository = heartRateRepository;
    }

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.routingkey}")
    private String routingkey;

    @RabbitListener(queues = "${spring.rabbitmq.queue}")
    public void receivedMessage(UserHeartRate userHeartRate) {
        logger.info("getting userHeartRate:" + userHeartRate);
        heartRateRepository.save(userHeartRate);
        logger.info("UserHeartRate recieved: " + userHeartRate);
    }
}
