package com.example.messenger.retrofit

import android.app.Application
import com.example.messenger.MyApplication
import com.example.messenger.repository.UserRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance(private val userRepository: UserRepository) {



        private val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(userRepository.getToken())).build()

        val api: ChatApi by lazy {
            Retrofit.Builder()
                .baseUrl("https://cedf-42-113-154-37.ngrok-free.app")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ChatApi::class.java)
        }
    }