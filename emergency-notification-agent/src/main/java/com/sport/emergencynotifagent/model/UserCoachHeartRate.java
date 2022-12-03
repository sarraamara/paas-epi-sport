package com.sport.emergencynotifagent.model;

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
