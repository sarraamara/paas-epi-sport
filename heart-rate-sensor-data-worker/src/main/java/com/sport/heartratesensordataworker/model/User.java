package com.sport.heartratesensordataworker.model;


import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

//    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("userId")
    Long userId;
    @JsonProperty("lastname")
    String lastname;
    @JsonProperty("firstname")
    String firstname;
    @JsonProperty("age")
    int age;
    @JsonProperty("weight")
    int weight;
    @JsonProperty("height")
    int height;

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", lastname='" + lastname + '\'' +
                ", firstname='" + firstname + '\'' +
                ", age=" + age +
                ", weight=" + weight +
                ", height=" + height +
                '}';
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
