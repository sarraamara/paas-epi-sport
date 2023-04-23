package com.sport.common.model;


import lombok.*;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserCoach {


    private int userCoachId;


    private UserProfile userProfile;


    private CoachProfile coachProfile;

}
