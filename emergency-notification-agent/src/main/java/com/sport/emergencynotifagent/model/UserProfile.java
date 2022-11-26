package com.sport.emergencynotifagent.model;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class UserProfile {

    @Id
    private int userId;

    private String lastname;
    private String firstname;
    private int age;
    private int weight;
    private int height;


}
