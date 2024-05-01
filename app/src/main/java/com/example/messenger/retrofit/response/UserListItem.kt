package com.example.messenger.retrofit.response

data class UserListItem(
    val account: String,
    val avatarImageUrl: String,
    val id: Int,
    val name: String
)