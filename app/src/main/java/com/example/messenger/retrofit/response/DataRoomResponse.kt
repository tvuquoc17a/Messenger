package com.example.messenger.retrofit.response

data class DataRoomResponse(
    val created: String,
    val createdBy: String,
    val domainEvents: List<Any>,
    val id: Int,
    val lastModified: String,
    val lastModifiedBy: String,
    val quantityUser: Int,
    val roomMessages: List<Any>,
    val roomUser: List<RoomUser>,
    val title: String
)