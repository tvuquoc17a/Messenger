package com.example.messenger.signalr

data class SignalrMessage(
    val Id : Int,
    val content : String,
    val UserSend : Int,
    val nameSend : String
)