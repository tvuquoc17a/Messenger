package com.example.messenger.viewmodel

import android.net.Uri
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
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class MainViewModel : ViewModel() {

    private var _messagesInRoom = MutableLiveData<List<Message>>()
    private var _onlineUserList = MutableLiveData<List<User>>()
    private var _latestMessageList = MutableLiveData<MutableList<Message>>()
    val onlineUserList: LiveData<List<User>>
        get() = _onlineUserList
    val messagesInRoom: LiveData<List<Message>>
        get() = _messagesInRoom

    val latestMessageList: LiveData<MutableList<Message>>
        get() = _latestMessageList

    fun fetchOnlineUsers() {
        FirebaseUtil.database.getReference("/Users").addValueEventListener(onlineUserListener)
    }

    fun getMessagesInRoom(roomId: String) {
        val messagesInRoomEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("getMessagesInRoom", "onDataChange: ${snapshot.value}")
                val messages = snapshot.children.mapNotNull { it.getValue(Message::class.java) }
                _messagesInRoom.value = messages
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("getMessagesInRoom", error.message)
            }

        }
        val ref =
            FirebaseUtil.database.getReference("Rooms/$roomId/messages").orderByChild("timestamp")
        ref.addValueEventListener(messagesInRoomEventListener)
    }

    suspend fun getOrCreateRoom(userId: String, partnerId: String, partnerName: String): String =
        withContext(Dispatchers.IO) {
            suspendCoroutine<String> { continuation ->
                val roomRef = FirebaseUtil.database.getReference("/Rooms")
                var roomId: String = roomRef.push().key.toString()

                val roomListener = object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (roomSnapshot in snapshot.children) {
                            val participantsSnapshot = roomSnapshot.child("participants")
                            val participants =
                                participantsSnapshot.children.mapNotNull { it.getValue(String::class.java) }

                            //Log.d("getOrCreateRoom", "participants: ${roomSnapshot.child("participants").getValue(HashMap::class.java)}")
                            if (participants.contains(userId) && participants.contains(partnerId) && participants.size == 2) {
                                roomId =
                                    roomSnapshot.child("id").getValue(String::class.java).toString()
                                continuation.resume(roomId)
                                roomRef.removeEventListener(this)
                                return
                            }
                        }

                        val userList = listOf(userId, partnerId)
                        val newRoom = Room(roomId, partnerName, userList, emptyList(), Message())
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

    fun sendMessage(roomId: String, content: String, partnerName: String, partnerImage: String) {
        val timestamp = System.currentTimeMillis()
        val messageId = UUID.randomUUID().toString()
        val message = UserSingleton.user?.value?.let {
            Message(
                messageId,
                UserSingleton.user!!.value?.uid!!,
                content,
                timestamp,
                UserSingleton.user!!.value?.profileUrl!!,
                UserSingleton.user!!.value?.name!!,
                roomId,
                MessageStatus.NONE,
                partnerImage,
                partnerName,
                null
            )
        }
        FirebaseUtil.database.getReference("/Rooms/$roomId/messages/$messageId").setValue(message)
        FirebaseUtil.database.getReference("/Rooms/$roomId/lastMessage/").setValue(message)
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

    suspend fun sendImage(roomId : String, partnerName : String, partnerImage: String,uri: String) {
        val timestamp = System.currentTimeMillis()
        val messageId = UUID.randomUUID().toString()
        val message = Message(
            messageId,
            UserSingleton.user!!.value?.uid!!,
            null,
            timestamp,
            UserSingleton.user!!.value?.profileUrl!!,
            UserSingleton.user!!.value?.name!!,
            roomId,
            MessageStatus.NONE,
            partnerImage,
            partnerName,
            uri
        )
        FirebaseUtil.database.getReference("/Rooms/$roomId/messages/$messageId").setValue(message)
        FirebaseUtil.database.getReference("/Rooms/$roomId/lastMessage/").setValue(message)
    }

     suspend fun getImageUrl(uri: Uri): String {
        val fileName = UUID.randomUUID().toString()
        val ref = FirebaseUtil.storage.getReference("/image_send/$fileName")
        return suspendCoroutine { continuation ->
            ref.putFile(uri)
                .addOnSuccessListener {
                    ref.downloadUrl
                        .addOnSuccessListener {
                            val imageUrl = it.toString()
                            continuation.resume(imageUrl)
                        }
                        .addOnFailureListener { exception ->
                            continuation.resumeWithException(exception)
                        }
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }

        }
    }

    fun getLatestMessage(userId: String) {
        val ref =
            FirebaseUtil.database.getReference("/Rooms").orderByChild("/lastMessage/timestamp")
        val roomListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lastMessages = mutableListOf<Message>()
                for (roomSnapshot in snapshot.children) {
                    val lastMessage =
                        roomSnapshot.child("lastMessage").getValue(Message::class.java)
                    val participantSnapshot = roomSnapshot.child("participants")
                    val participants =
                        participantSnapshot.children.mapNotNull { it.getValue(String::class.java) }
                    if (participants.contains(userId)) {
                        if (lastMessage != null) {
                            lastMessages.add(lastMessage)
                        }
                    }
                }
                _latestMessageList.value = lastMessages

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("getLatestMessage", error.message)
            }

        }
        ref.addValueEventListener(roomListener)
    }
}