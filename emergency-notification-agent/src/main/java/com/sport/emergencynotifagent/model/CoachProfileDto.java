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
@Table(name = "coachProfile")
public class CoachProfileDto {

    @Id
    @Column(name = "coachId")
    private int coachId;

    private String lastname;
    private String firstname;
}

