package com.example.messenger.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.messenger.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.database
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val database by lazy { Firebase.database }
    private val storage by lazy { FirebaseStorage.getInstance() }
    private lateinit var currentUser: FirebaseUser
    val errorNotification = MutableLiveData<String>()
    private var profileImageUrl = String()

    fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener() {
                if (it.isSuccessful) {
                    currentUser = auth.currentUser!!
                    Log.d("login", currentUser.uid)
                }
            }
    }

    fun currentUserUid(): String {
        return currentUser.uid
    }

    fun signUp(email: String, password: String, name: String, profileUrl: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener() {
                currentUser = auth.currentUser!!
                addUserToDataBase(name, email, profileUrl)
                Log.d("sign_up", "success")
            }
            .addOnFailureListener() {
                if (it is FirebaseAuthUserCollisionException) {
                    errorNotification.value = "Email already in use"
                }
                else{
                    Log.d("sign_up", "failed")
                }
            }

    }


    suspend fun getProfileUrl(uri: Uri): String {
        val fileName = UUID.randomUUID().toString()
        val ref = storage.getReference("/profile_images/$fileName")
        return suspendCoroutine<String> {continuation ->
            ref.putFile(uri)
                .addOnSuccessListener {
                    ref.downloadUrl
                        .addOnSuccessListener { uri ->
                            profileImageUrl = uri.toString()
                            continuation.resume(profileImageUrl)
                            Log.d("profile_url", profileImageUrl)
                        }
                        .addOnFailureListener{exception ->
                            continuation.resumeWithException(exception)
                        }
                }
                .addOnFailureListener{exception ->
                    continuation.resumeWithException(exception)
                }

        }

    }

    private fun addUserToDataBase(name: String, email: String, profileUrl: String) {
        val user = User(currentUserUid(), name, email, profileUrl)
        val databaseRef = database.getReference("Users/${currentUserUid()}")
        databaseRef.setValue(user)
    }
}


