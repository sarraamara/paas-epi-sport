package com.sport.heartratesensordatacollector.service;


import com.sport.common.model.UserHeartRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class HeartRateService {


    @Autowired
    private KafkaTemplate<String, UserHeartRate> kafkaTemplate;

    public void sendMessage(String topic, UserHeartRate payload) {
        kafkaTemplate.send(topic, payload);
    }

}
