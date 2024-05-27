package com.example.messenger.singleton

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.messenger.model.User

object UserSingleton {
    var user : MutableLiveData<User>? = MutableLiveData()
}