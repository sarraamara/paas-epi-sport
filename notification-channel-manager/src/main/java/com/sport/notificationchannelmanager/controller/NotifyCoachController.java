package com.sport.notificationchannelmanager.controller;

import com.sport.notificationchannelmanager.Service.KafkaCustomConsumer;
import com.sport.notificationchannelmanager.repository.NotifyCoachRepository;
import com.sport.notificationchannelmanager.model.Coach;
import com.sport.notificationchannelmanager.model.UserCoachHeartRate;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.logging.Logger;


@RestController
@RequestMapping("/notification")
public class NotifyCoachController {

    @Autowired
    NotifyCoachRepository notifyCoachRepository;

    @Autowired
    KafkaCustomConsumer kafkaConsumer;

    private static final Logger LOGGER = Logger.getLogger(NotifyCoachController.class.getName());

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "notif-topic", groupId = "ncc")
    public void listen(ConsumerRecord<String, Object> record) {
        // Traitement du message reçu
        System.out.println(record.value());
    }

    @PostMapping("/save-session/{coachId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveSession(@PathVariable String coachId) {
        LOGGER.info("SAVING SESSION, COACHID="+coachId);
        kafkaTemplate.send("coach"+coachId+"-topic","present");
    }

    @PostMapping("/del-session/{coachId}")
    public void delSession(@PathVariable String coachId) {
        LOGGER.info("DELETE SESSION, COACHID="+coachId);
        kafkaTemplate.send("coach"+coachId+"-topic", "absent");
    }
    @GetMapping("/get-session/{coachId}")
    public Coach getCoachSession(@PathVariable String coachId) {
        Coach coach = new Coach(kafkaConsumer.getLastMessage("coach"+coachId+"-topic").value());
        LOGGER.info(coach.toString());
        return coach;
    }
    @GetMapping("/get-session")
    public Iterable<Coach> getSessions() {
        List<Coach> coaches = null;
        for (int i = 0; i < 10; i++) {
            Coach coach = new Coach(kafkaConsumer.getLastMessage("coach"+i+"-topic").value());
            coaches.add(coach);
        }
        return coaches;
    }

    @GetMapping("/get-notif/{coachId}")
    public UserCoachHeartRate getNotification(@PathVariable int coachId) {
        String topic_name = "notif-topic";
        Object userCoachHeartRate = kafkaConsumer.getLastMessage(topic_name).value();

        return (UserCoachHeartRate) userCoachHeartRate;
    }

}
