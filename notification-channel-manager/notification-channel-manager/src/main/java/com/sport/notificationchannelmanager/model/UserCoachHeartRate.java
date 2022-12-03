package com.sport.notificationchannelmanager.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserCoachHeartRate {

    private UserCoach userCoach;
    private int heartRate;
}
