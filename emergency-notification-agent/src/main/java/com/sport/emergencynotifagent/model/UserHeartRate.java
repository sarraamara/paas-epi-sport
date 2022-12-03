package com.sport.emergencynotifagent.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class UserHeartRate {
    private String userId;
    private int heartRate;
}

