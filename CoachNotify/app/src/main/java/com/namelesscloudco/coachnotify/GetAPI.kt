package com.namelesscloudco.coachnotify

import com.namelesscloudco.coachnotify.model.UserCoachHeartRate
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface GetAPI {
    @POST("/notification/save-session/{coachId}")
    fun notify(@Path("coachId") coachId: String) : Call<String>

    @POST("/notification/del-session/{coachId}")
    fun delete(@Path("coachId") coachId: String) : Call<String>

    @GET("/notification/get-notif/{coachId}")
    fun fetchNotif(@Path("coachId") coachId: Int) : Call<UserCoachHeartRate>


}