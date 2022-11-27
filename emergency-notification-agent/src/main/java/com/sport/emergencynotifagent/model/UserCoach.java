package com.sport.emergencynotifagent.model;

import lombok.*;


import javax.persistence.*;

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

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserProfile userProfile;

    @ManyToOne
    @JoinColumn(name = "coachId")
    private CoachProfile coachProfile;

}
