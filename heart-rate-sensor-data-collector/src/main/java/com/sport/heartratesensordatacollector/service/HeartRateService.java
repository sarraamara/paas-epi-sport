package com.sport.heartratesensordatacollector.service;

import com.sport.heartratesensordatacollector.model.UserHeartRate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HeartRateService {

    private RabbitTemplate rabbitTemplate;

    @Autowired
    public HeartRateService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.routingkey_hr}")
    private String routingkey;

    public void sendMessage(UserHeartRate userHeartRate) {
        rabbitTemplate.convertAndSend(exchange,routingkey, userHeartRate);
    }
}
