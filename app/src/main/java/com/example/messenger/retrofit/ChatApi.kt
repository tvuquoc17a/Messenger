package com.example.messenger.retrofit

import com.example.messenger.model.SignUpUser
import com.example.messenger.model.LoginUser
import com.example.messenger.model.RegisterResponse
import com.example.messenger.retrofit.response.LoginResponse
import com.example.messenger.retrofit.response.UserListItem
import com.example.messenger.retrofit.response.UserResponse
import retrofit2.http.POST
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET

interface ChatApi {

    @POST("api/Login/register")
    fun createUser(@Body user: SignUpUser) : Call<RegisterResponse>

    @POST("api/Login/login")
    fun login(@Body loginUser: LoginUser) : Call<LoginResponse>

    @GET("api/User")
    fun getUserList() : Call<List<UserListItem>>
}