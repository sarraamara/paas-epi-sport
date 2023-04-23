package com.sport.common.model;

import lombok.*;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserProfile {


    private int userId;

    private String lastname;
    private String firstname;
    private int age;
    private int weight;
    private int height;


}
