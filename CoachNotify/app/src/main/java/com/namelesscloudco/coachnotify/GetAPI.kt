package com.namelesscloudco.coachnotify

import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface GetAPI {
    @POST("/notification/save-session/{coachId}")
    fun notify(@Path("coachId") coachId: String) : Call<String>

    @POST("/notification/del-session/{coachId}")
    fun delete(@Path("coachId") coachId: String) : Call<String>

}