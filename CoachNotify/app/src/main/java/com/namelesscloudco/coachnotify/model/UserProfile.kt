package com.namelesscloudco.coachnotify.model

data class UserProfile(
    val userId: Int,
    val lastname: String,
    val firstname: String,
    val age: Int,
    val height: Int,
    val weight: Int
)