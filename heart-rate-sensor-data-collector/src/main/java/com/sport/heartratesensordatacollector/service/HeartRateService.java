package com.sport.heartratesensordatacollector.service;

import com.sport.heartratesensordatacollector.controller.HeartRateController;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class HeartRateService {
    private static final Logger logger = LoggerFactory.getLogger(HeartRateController.class);

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    @KafkaListener(topics = "hrdata-topic", groupId = "ncc")
    public void listen(ConsumerRecord<String, Object> record) {
        logger.info("GETTING NEW MESSAGE");
        // Traitement du message reçu
        logger.info(record.value().toString());;
    }


    public void sendMessage(String topic, Object payload) {
        kafkaTemplate.send(topic, payload);
    }

}
