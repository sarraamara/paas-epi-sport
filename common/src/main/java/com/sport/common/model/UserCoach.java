package com.sport.common.model;

import jakarta.persistence.*;
import lombok.*;



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
