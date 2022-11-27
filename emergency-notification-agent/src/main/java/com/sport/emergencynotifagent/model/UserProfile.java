package com.sport.emergencynotifagent.model;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "userProfile")
public class UserProfile {

    @Id
    @Column(name = "userId")
    private int userId;

    private String lastname;
    private String firstname;
    private int age;
    private int weight;
    private int height;


}
