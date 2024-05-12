package com.example.messenger.retrofit

import com.example.messenger.model.SignUpUser
import com.example.messenger.model.LoginUser
import com.example.messenger.model.RegisterResponse
import com.example.messenger.retrofit.request.Message
import com.example.messenger.retrofit.request.RoomInfo
import com.example.messenger.retrofit.response.CreateMessageResponse
import com.example.messenger.retrofit.response.CreateRoomResponse
import com.example.messenger.retrofit.response.LoginResponse
import com.example.messenger.retrofit.response.UserListItem
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

    @POST("api/Room/Check")
    fun createRoom(@Body roomInfo: RoomInfo) : Call<CreateRoomResponse>

    @POST("api/Messages/Create")
    fun sendMessage(@Body message: Message) : Call<CreateMessageResponse>

    @POST("api/Messages/Get")
    fun getMessages(@Body roomId: Int) : Call<List<Message>>
}