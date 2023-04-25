package com.sport.notificationchannelmanager.controller;

import com.sport.common.model.UserCoachHeartRate;
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
    private RedisTemplate<String, String> Redistemplate;

    private static final Logger LOGGER = Logger.getLogger(NotifyCoachController.class.getName());

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private final List<UserCoachHeartRate> userCoachHeartRates = new ArrayList<>();

    @PostMapping("/save-session/{coachId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveSession(@PathVariable String coachId) {
        LOGGER.info("SAVING SESSION, COACHID="+coachId);
        Redistemplate.opsForValue().set(STRING_KEY_PREFIX + coachId, "coach"+coachId,10, TimeUnit.MINUTES);
    }

    @PostMapping("/del-session/{coachId}")
    public void delSession(@PathVariable String coachId) {
        LOGGER.info("DELETE SESSION, COACHID="+coachId);
        Redistemplate.opsForValue().getAndDelete(STRING_KEY_PREFIX + coachId);
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
        for (UserCoachHeartRate userCoachHeartRate : userCoachHeartRates) {
            if (userCoachHeartRate.getUserCoach().getCoachProfile().getCoachId() == coachId) {
                userCoachHeartRates.remove(userCoachHeartRate);
                return userCoachHeartRate;
            }
        }
        return null;
    }

    @KafkaListener(topics = "notif-topic", groupId = "ncc", containerFactory = "userHeartRateListener")
    public void receivedMessage(UserCoachHeartRate userCoachHeartRate) {
        LOGGER.info("Received emergency related to user :" + userCoachHeartRate);
        userCoachHeartRates.add(userCoachHeartRate);
    }
}
