package com.example.messenger.repository

import android.content.Context

class UserRepository(private val context: Context) {

    private val prefsName = "MyPrefs"

    fun saveToken(token: String){
        val prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("token", token)
        editor.apply()
    }

    fun getToken(): String? {
        val prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        return prefs.getString("token", null)
    }

    fun clearToken(){
        val prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.remove("token")
        editor.apply()
    }
}