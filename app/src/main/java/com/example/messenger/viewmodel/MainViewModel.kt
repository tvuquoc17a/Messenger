package com.example.messenger.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.messenger.model.Message
import com.example.messenger.model.MessageStatus
import com.example.messenger.model.Room
import com.example.messenger.model.User
import com.example.messenger.repository.FirebaseUtil
import com.example.messenger.singleton.UserSingleton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class MainViewModel : ViewModel() {

    private var _messagesInRoom = MutableLiveData<List<Message>>()
    private var _onlineUserList = MutableLiveData<List<User>>()
    val onlineUserList: LiveData<List<User>>
        get() = _onlineUserList
    val messagesInRoom: LiveData<List<Message>>
        get() = _messagesInRoom

    fun fetchOnlineUsers() {
        FirebaseUtil.database.getReference("/Users").addValueEventListener(onlineUserListener)
    }

    fun getMessagesInRoom(roomId : String){
        val messagesInRoomEventListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("getMessagesInRoom", "onDataChange: ${snapshot.value}")
                val messages = snapshot.children.mapNotNull { it.getValue(Message::class.java) }
                _messagesInRoom.value = messages
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("getMessagesInRoom", error.message)
            }

        }
        val ref = FirebaseUtil.database.getReference("Rooms/$roomId/messages").orderByChild("timestamp")
        ref.addValueEventListener(messagesInRoomEventListener)
    }

    suspend fun getOrCreateRoom(userId: String, partnerId: String): String = withContext(Dispatchers.IO) {
            suspendCoroutine<String> { continuation ->
                val roomRef = FirebaseUtil.database.getReference("/Rooms")
                var roomId: String = roomRef.push().key.toString()

                val roomListener = object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (roomSnapshot in snapshot.children) {
                            val participantsSnapshot = roomSnapshot.child("participants")
                            val participants = participantsSnapshot.children.mapNotNull { it.getValue(String::class.java) }

                            //Log.d("getOrCreateRoom", "participants: ${roomSnapshot.child("participants").getValue(HashMap::class.java)}")
                            if (participants.contains(userId) && participants.contains(partnerId) && participants.size == 2) {
                                roomId = roomSnapshot.child("id").getValue(String::class.java).toString()
                                continuation.resume(roomId)
                                roomRef.removeEventListener(this)
                                return
                            }
                        }

                        val userList = listOf(userId, partnerId)
                        val newRoom = Room(roomId, userList, emptyList(), Message())
                        roomRef.child(roomId).setValue(newRoom)
                        continuation.resume(roomId)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("MainViewModel", "getOrCreateRoomId:onCancelled", error.toException())
                        continuation.resumeWithException(error.toException())
                    }

                }
                roomRef.addListenerForSingleValueEvent(roomListener)
            }

        }

    fun sendMessage(roomId: String, content: String) {
        val timestamp = System.currentTimeMillis()
        val messageId = UUID.randomUUID().toString()
        val message = UserSingleton.user?.value?.let {
            Message(messageId, UserSingleton.user?.value?.uid.toString(), content,timestamp,
                it.profileUrl, roomId,MessageStatus.SENT )
        }
        FirebaseUtil.database.getReference("/Rooms/$roomId/messages/$messageId").setValue(message)
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