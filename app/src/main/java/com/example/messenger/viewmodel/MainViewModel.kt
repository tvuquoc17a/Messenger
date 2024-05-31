package com.example.messenger.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.messenger.model.User
import com.example.messenger.repository.FirebaseUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MainViewModel : ViewModel() {

    private var _onlineUserList = MutableLiveData<List<User>>()
    val onlineUserList: LiveData<List<User>>
        get() = _onlineUserList

    fun fetchOnlineUsers() {
        FirebaseUtil.database.getReference("/Users").addValueEventListener(onlineUserListener)
    }

    private val onlineUserListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            _onlineUserList.value = snapshot.children.map {
                it.getValue(User::class.java)!!
            }
            Log.d("MainViewModel", "onDataChange: ${_onlineUserList.value}")
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("MainViewModel", "onCancelled: ${error.message}")
        }
    }
}