package com.sport.heartratesensordatacollector.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Component
public class UserHeartRate implements Serializable {
    private String id;
    private String userId;
    private int heartRate;
}
