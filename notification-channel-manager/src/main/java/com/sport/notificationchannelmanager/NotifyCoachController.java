package com.sport.notificationchannelmanager;

import com.sport.notificationchannelmanager.model.UserCoach;
import com.sport.notificationchannelmanager.model.UserCoachHeartRate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
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

    Map<Integer, ArrayList<UserCoachHeartRate>> usersNotification = new HashMap<>();
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
        rabbitTemplate.receiveAndConvert();
        ArrayList<UserCoachHeartRate> userNotif =  null;
        UserCoachHeartRate notifContent = null;
        if(usersNotification.containsKey(coachId)){
            userNotif = usersNotification.get(coachId);
            if(!userNotif.isEmpty()){
                notifContent = userNotif.get(0);
                userNotif.remove(0);
                return notifContent;
            }
        }

        return null;
    }
    @RabbitListener(queues = "${spring.rabbitmq.queue_notif}")
    public void receivedMessage(UserCoachHeartRate notifContent) {
        LOGGER.info(notifContent.toString());

        int coachId = notifContent.getUserCoach().getCoachProfile().getCoachId();

        if(usersNotification.containsKey(coachId)){
           ArrayList<UserCoachHeartRate> userNotif = usersNotification.get(coachId);
           userNotif.add(notifContent);
           usersNotification.put(coachId, userNotif);
        }
        else {
            ArrayList<UserCoachHeartRate> userNotif = new ArrayList<>();
            userNotif.add(notifContent);
            usersNotification.put(coachId, userNotif);
        }
    }

}
