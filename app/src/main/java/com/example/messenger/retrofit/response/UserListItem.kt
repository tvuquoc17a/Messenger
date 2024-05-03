package com.example.messenger.retrofit.response

import java.io.Serializable

data class UserListItem(
    val account: String,
    val avatarImageUrl: String,
    val id: Int,
    val name: String
) : Serializable