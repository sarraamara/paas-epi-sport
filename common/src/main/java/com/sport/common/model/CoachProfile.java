package com.sport.common.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "coachProfile")
public class CoachProfile {

    @Id
    @Column(name = "coachId")
    private int coachId;

    private String lastname;
    private String firstname;
}
