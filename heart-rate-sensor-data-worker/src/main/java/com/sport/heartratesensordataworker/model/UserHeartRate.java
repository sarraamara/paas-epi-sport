package com.sport.heartratesensordataworker.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Document("heart_rate")
public class UserHeartRate {
    @Id
    private String id;

    private String userId;
    private int heartRate;
}
