package com.sport.mock;

import com.sport.mock.model.UserHeartRate;
import com.sport.mock.model.UserProfile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Random;
import java.util.logging.Logger;

public class UserExerciceThread extends Thread{


    private UserProfile userProfile;

    private RestTemplate restemp = new RestTemplate();

    private final int AVG_HEART_RATE = 72;

    private final String IP = "172.31.250.71";

    private static final Logger logger = Logger.getLogger(MockApplication.class.getName());

    private final String URL_SENSOR_HEART_RATE =
            "http://"+IP+":8081/sensor/hr/produce";

    public UserExerciceThread(UserProfile userProfile){
        this.userProfile = userProfile;
    }

    public void run(){
        Random r = new Random();
        boolean resting = false;
        int max_period_sprint=20000; //30 secondes
        int milliseconde_timer=0;
        int max_resting=30000;
        int heartRatePeakZone = (int) (211 - 0.64*userProfile.getAge());
        int heartRate= AVG_HEART_RATE;

        UserHeartRate userHeartRate = new UserHeartRate(userProfile.getUserId(), heartRate);

        while(true){

            if((heartRate>=(heartRatePeakZone-5) ) && (heartRate<=(heartRatePeakZone+5)) && !resting){
                heartRate = heartRatePeakZone;

                if(milliseconde_timer==0) {
                    logger.info(userProfile.getLastname() + " is reaching her/his peak..");
                }
                milliseconde_timer+=1000;
                if(milliseconde_timer==max_period_sprint){
                    milliseconde_timer=0;
                    heartRate=heartRatePeakZone+6;
                }
            }
            else if (!resting){
                heartRate+= r.nextInt(9) + 1;
            }
            else {
                heartRate-= r.nextInt(9) + 1;
            }

            if((heartRate >= (heartRatePeakZone + 20)) && (!resting)){
                logger.info(userProfile.getLastname()+" is lowering her/his rythm..");
                resting=true;
            }

            if(resting){
                milliseconde_timer+=1000;
                if(milliseconde_timer==max_resting){
                    resting=false;
                    milliseconde_timer=0;
                    logger.info(userProfile.getLastname()+" is accelerating her/his rythm..");
                }
            }

            userHeartRate.setHeartRate(heartRate);
            ResponseEntity response = restemp.exchange(
                URL_SENSOR_HEART_RATE,
                HttpMethod.POST,
                new HttpEntity<>(userHeartRate),
                String.class
            );
            if(response.getStatusCodeValue() == 200){
                logger.info(userProfile.getLastname() + "'s heart rate is " + heartRate+"bpm.");
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
