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
public class CoachProfile {

    @Id
    private int coachId;

    private String lastname;
    private String firstname;
}
