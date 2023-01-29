package com.fanyfernaldi.pitjarustest.services

import com.fanyfernaldi.pitjarustest.requests.LoginRequest
import com.fanyfernaldi.pitjarustest.responses.LoginResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("login/loginTest")
    fun login(@Body request: RequestBody): Call<LoginResponse>
}