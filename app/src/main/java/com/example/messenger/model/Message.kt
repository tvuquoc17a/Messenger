package com.example.messenger.model

data class Message(
    val id: String,
    val senderId: String,
    val content: String,
    val timestamp: Long,
    val senderImage: String,
    val senderName : String,
    val roomId: String,
    val status: MessageStatus,
    val partnerImage : String,
    val partnerName : String
) {
    //create a default constructor
    constructor() : this("", "", "", 0L, "","","", MessageStatus.NONE, "","")
}
