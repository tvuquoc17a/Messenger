package com.example.messenger.retrofit.response

data class CreateMessageResponse(
    val `data`: DataMessageRespose,
    val error: Any,
    val succeeded: Boolean
)