package com.example.messenger.model

import java.util.SortedSet

data class Room(
    val id: String ,
    val participants : List<String>,
    val messages : List<Message>,
    val lastMessage: Message
){
    constructor() : this("", emptyList(), emptyList(), Message())
}
