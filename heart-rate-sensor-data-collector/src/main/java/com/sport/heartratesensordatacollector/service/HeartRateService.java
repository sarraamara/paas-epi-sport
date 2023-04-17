package com.sport.heartratesensordatacollector.service;

import com.sport.heartratesensordatacollector.model.UserHeartRate;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class HeartRateService {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    @KafkaListener(topics = "hrdata-topic", groupId = "ncc")
    public void listen(ConsumerRecord<String, Object> record) {
        // Traitement du message reçu
        System.out.println(record.value());
    }


    public void sendMessage(String topic, Object payload) {
        kafkaTemplate.send(topic, payload);
    }

}
