package com.example.messenger.retrofit.response

data class CreateRoomResponse(
    val `data`: CreateRoomData,
    val error: Any,
    val succeeded: Boolean
)