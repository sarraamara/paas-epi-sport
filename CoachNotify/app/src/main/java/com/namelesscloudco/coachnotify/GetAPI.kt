package com.namelesscloudco.coachnotify

import retrofit2.Call
import retrofit2.http.PUT
import retrofit2.http.Path


interface GetAPI {
    @PUT("/notification/save_session/{coachId}")
    fun notify(@Path("coachId") coachId: String) : Call<String>
}