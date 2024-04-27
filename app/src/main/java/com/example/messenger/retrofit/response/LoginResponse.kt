package com.example.messenger.retrofit.response

data class LoginResponse(
    val `data`: Data,
    val error: Any,
    val succeeded: Boolean
)