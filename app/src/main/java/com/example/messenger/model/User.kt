package com.example.messenger.model

data class User(
    val uid: String,
    val name: String,
    val email : String,
    val profileUrl: String
) : java.io.Serializable{
    constructor() : this("","","","")
}
