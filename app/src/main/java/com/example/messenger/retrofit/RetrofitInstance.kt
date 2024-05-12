package com.example.messenger.retrofit

import com.example.messenger.Constants
import com.example.messenger.repository.UserRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance(userRepository: UserRepository) {



        private val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(userRepository.getToken())).build()
        val api: ChatApi by lazy {
            Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_APP)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ChatApi::class.java)
        }
    }