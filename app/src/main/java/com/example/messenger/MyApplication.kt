package com.example.messenger

import android.app.Application
import com.example.messenger.repository.UserRepository

class MyApplication : Application() {

    lateinit var userRepository: UserRepository
    companion object{
        lateinit var instance : MyApplication
            private set
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}