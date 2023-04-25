package com.sport.notificationchannelmanager.controller;

import com.sport.common.model.UserCoachHeartRate;
import com.sport.notificationchannelmanager.service.KafkaCustomConsumer;
import com.sport.notificationchannelmanager.repository.NotifyCoachRepository;
import com.sport.notificationchannelmanager.model.Coach;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


@RestController
@RequestMapping("/notification")
public class NotifyCoachController {
    private static final String STRING_KEY_PREFIX = "coach:";
    @Autowired
    NotifyCoachRepository notifyCoachRepository;

    @Autowired
    KafkaCustomConsumer kafkaConsumer;

    @Autowired
    private RedisTemplate<String, String> Redistemplate;

    private static final Logger LOGGER = Logger.getLogger(NotifyCoachController.class.getName());

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;


    @PostMapping("/save-session/{coachId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveSession(@PathVariable String coachId) {
        LOGGER.info("SAVING SESSION, COACHID="+coachId);
        Redistemplate.opsForValue().set(STRING_KEY_PREFIX + coachId, "coach"+coachId,10, TimeUnit.MINUTES);
        kafkaTemplate.send("coach"+coachId+"-topic", "present");
    }

    @PostMapping("/del-session/{coachId}")
    public void delSession(@PathVariable String coachId) {
        LOGGER.info("DELETE SESSION, COACHID="+coachId);
        Redistemplate.opsForValue().getAndDelete(STRING_KEY_PREFIX + coachId);
        kafkaTemplate.send("coach"+coachId+"-topic", "absent");
    }
    @GetMapping("/get-session/{coachId}")
    public Optional<Coach> getCoachSession(@PathVariable String coachId) {
        Optional<Coach> coach = Optional.of(new Coach(Redistemplate.opsForValue().get(STRING_KEY_PREFIX + coachId)));
        LOGGER.info(coach.toString());
        return coach;
    }
    @GetMapping("/get-session")
    public Iterable<Coach> getSessions() {
        Set<String> redisKeys = Redistemplate.keys("*");
        List<Coach> coaches = new ArrayList<>();
        assert redisKeys != null;
        for (String redisKey : redisKeys) {
            Coach coach = new Coach(redisKey);
            coaches.add(coach);
        }
        return coaches;
    }

    @GetMapping("/get-notif/{coachId}")
    public UserCoachHeartRate getNotification(@PathVariable int coachId) {
        String topic_name = "coach"+coachId+"-topic";
        System.out.println(kafkaConsumer.getLastMessage(topic_name).value());
        Object userCoachHeartRate = kafkaConsumer.getLastMessage(topic_name).value();

        return (UserCoachHeartRate) userCoachHeartRate;
    }
}
