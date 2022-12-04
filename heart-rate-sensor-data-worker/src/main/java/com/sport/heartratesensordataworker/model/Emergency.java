package com.sport.heartratesensordataworker.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Document("emergency")
public class Emergency {
    @Id
    String id;
    String userId;
    int heartRate;
    int heartRateMax;
    Timestamp timestamp;
}
