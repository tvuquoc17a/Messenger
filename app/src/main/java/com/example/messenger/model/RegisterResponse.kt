package com.example.messenger.model

data class RegisterResponse(
    val `data`: Int,
    val error: Error,
    val succeeded: Boolean
)