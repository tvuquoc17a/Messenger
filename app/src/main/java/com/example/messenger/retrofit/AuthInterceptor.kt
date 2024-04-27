package com.example.messenger.retrofit

import okhttp3.Interceptor

class AuthInterceptor(private val token: String?) : Interceptor {
    //chặn request ban đầu sau đó thêm vào header token và trả về request mới
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val originalRequest = chain.request()

        val newRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        if (token == null) {
            return chain.proceed(originalRequest)
        }
        return chain.proceed(newRequest)
    }
}