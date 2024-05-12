package com.example.messenger.retrofit.request

data class Message(
    val content: String,
    val roomId: Int,
    val userId: Int
)