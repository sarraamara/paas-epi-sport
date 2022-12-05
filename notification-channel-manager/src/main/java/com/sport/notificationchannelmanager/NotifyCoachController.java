package com.sport.notificationchannelmanager;

import com.sport.notificationchannelmanager.model.UserCoachHeartRate;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


@RestController
@RequestMapping("/notification")
public class NotifyCoachController {
    @Autowired
    private RedisTemplate<String, String> template;

    @Autowired
    private RabbitTemplate rabbitTemplate;
    private static final String STRING_KEY_PREFIX = "coach:";
    @Autowired
    NotifyCoachRepository notifyCoachRepository;

    @Autowired
    private RabbitAdmin admin;

    private static final Logger LOGGER = Logger.getLogger(NotifyCoachController.class.getName());
    @PostMapping("/save-session/{coachId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveSession(@PathVariable String coachId) {
        LOGGER.info("SAVING SESSION, COACHID="+coachId);
        template.opsForValue().set(STRING_KEY_PREFIX + coachId, "coach"+coachId,10, TimeUnit.MINUTES);
    }

    @PostMapping("/del-session/{coachId}")
    public void delSession(@PathVariable String coachId) {
        LOGGER.info("DELETE SESSION, COACHID="+coachId);
        template.opsForValue().getAndDelete(STRING_KEY_PREFIX + coachId);
        admin.purgeQueue("coachId" + coachId,true);
    }
    @GetMapping("/get-session/{coachId}")
    public Optional<Coach> getCoachSession(@PathVariable String coachId) {
        Optional<Coach> coach = Optional.of(new Coach(template.opsForValue().get(STRING_KEY_PREFIX + coachId)));
        LOGGER.info(coach.toString());
        return coach;
    }
    @GetMapping("/get-session")
    public Iterable<Coach> getSessions() {
        Set<String> redisKeys = template.keys("*");
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
        String queue_name = "coachId" + coachId;
        Object userCoachHeartRate = rabbitTemplate.receiveAndConvert(queue_name);

        return (UserCoachHeartRate) userCoachHeartRate;
    }

}
