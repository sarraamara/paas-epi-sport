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
@Table(name = "userCoach")
public class UserCoach {

    @Id
    @Column(name="userCoachId")
    private int userCoachId;

    private int userId;
    private int coachId;

}
