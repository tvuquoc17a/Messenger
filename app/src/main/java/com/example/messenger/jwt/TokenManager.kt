package com.example.messenger.jwt

import com.auth0.android.jwt.JWT

object TokenManager {
    private const val NAME = "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/nameidentifier"

    fun decodeToken(token: String): JWT {
        return JWT(token)
    }

    fun getUserId(token: String): Int {
        return decodeToken(token).getClaim(NAME).asString()?.toInt() ?: 0
    }

    fun getUserName(token: String): String {
        return decodeToken(token).getClaim("name").asString().toString()
    }
    fun getAudience(token : String) : String{
        return decodeToken(token).audience.toString()
    }

}