package com.example.messenger.retrofit

import com.example.messenger.model.SignUpUser
import com.example.messenger.model.LoginUser
import com.example.messenger.retrofit.response.LoginResponse
import retrofit2.http.POST
import retrofit2.Call
import retrofit2.http.Body

interface ChatApi {

    @POST("api/Login/register")
    fun createUser(@Body user: SignUpUser) : Call<String>

    @POST("api/Login/login")
    fun login(@Body loginUser: LoginUser) : Call<LoginResponse>
}