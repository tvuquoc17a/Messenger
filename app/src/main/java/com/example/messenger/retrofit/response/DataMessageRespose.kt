package com.example.messenger.retrofit.response

data class DataMessageRespose(
    val content: String,
    val created: String,
    val createdBy: String,
    val domainEvents: List<Any>,
    val id: Int,
    val lastModified: String,
    val lastModifiedBy: String,
    val user: Any,
    val userId: Int
)