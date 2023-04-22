package com.sport.heartratesensordatacollector.controller;


import com.sport.heartratesensordatacollector.model.UserHeartRate;
import com.sport.heartratesensordatacollector.service.HeartRateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sensor/hr/")
public class HeartRateController {

    private final HeartRateService heartRateService;
    private static final Logger logger = LoggerFactory.getLogger(HeartRateController.class);

    @Autowired
    public HeartRateController(HeartRateService heartRateService){
        this.heartRateService = heartRateService;
    }

    @Value("${app.message}")
    private String response;

    @PostMapping("/produce")
    public ResponseEntity<String> sendMessage(@RequestBody UserHeartRate userHeartRate) {
        logger.info("getting userHeartRate:" + userHeartRate);
        heartRateService.sendMessage("hrdata-topic",userHeartRate);
        logger.info("SmartWatch sent: " + userHeartRate);
        return ResponseEntity.ok(response);
    }
}
