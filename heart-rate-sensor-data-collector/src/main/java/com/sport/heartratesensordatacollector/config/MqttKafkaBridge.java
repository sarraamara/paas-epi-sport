package com.sport.heartratesensordatacollector.config;

import com.google.gson.Gson;
import com.sport.common.model.UserHeartRate;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class MqttKafkaBridge  {

    @Value("${mqtt.broker.url}")
    private final String BROKER_URL = "tcp://192.168.1.8:1883";
    @Value("${mqtt.topic}")
    private final String TOPIC = "hrdata-topic";
    Gson gson = new Gson();
    private static final Logger logger = LoggerFactory.getLogger(MqttKafkaBridge.class);
    @Autowired
    private KafkaTemplate<String, UserHeartRate> kafkaTemplate;

    @Bean
    public void start() throws MqttException {
        MqttClient mqttClient = new MqttClient(BROKER_URL, MqttClient.generateClientId());
        mqttClient.connect(new MqttConnectOptions());
        mqttClient.subscribe(TOPIC, (topic, message) -> {
            String payload = new String(message.getPayload());
            logger.info("Received message from MQTT: " + payload);
            kafkaTemplate.send(TOPIC, gson.fromJson(payload, UserHeartRate.class));
        });
    }
}

