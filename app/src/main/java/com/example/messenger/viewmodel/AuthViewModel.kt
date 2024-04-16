package com.example.messenger.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.messenger.model.User
import com.google.firebase.Firebase
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.database
import com.google.firebase.storage.FirebaseStorage
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
    val loginStatus = MutableLiveData<Boolean>()

    suspend fun loginUser(email: String, password: String): String? {
        return suspendCoroutine { continuation ->
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener() {
                    if (it.isSuccessful) {
                        loginStatus.value = true
                        currentUser = auth.currentUser!!
                        Log.d("sign_in", currentUser.uid)
                        continuation.resume(null)
                    }
                }.addOnFailureListener() {
                    loginStatus.value = false
                    var loginError : String? = null
                    when(it){
                        is FirebaseAuthInvalidCredentialsException -> loginError = "Invalid credentials"
                        is FirebaseAuthInvalidUserException -> loginError = "No user corresponding to the given email"
                        is FirebaseNetworkException -> loginError = "Network error"
                        is FirebaseTooManyRequestsException -> loginError = "Too many requests"
                    }
                    continuation.resume(loginError)
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
                } else {
                    Log.d("sign_up", "failed")
                }
            }

    }


    suspend fun getProfileUrl(uri: Uri): String {
        val fileName = UUID.randomUUID().toString()
        val ref = storage.getReference("/profile_images/$fileName")
        return suspendCoroutine<String> { continuation ->
            ref.putFile(uri)
                .addOnSuccessListener {
                    ref.downloadUrl
                        .addOnSuccessListener { uri ->
                            profileImageUrl = uri.toString()
                            continuation.resume(profileImageUrl)
                            Log.d("profile_url", profileImageUrl)
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

    private fun addUserToDataBase(name: String, email: String, profileUrl: String) {
        val user = User(currentUserUid(), name, email, profileUrl)
        val databaseRef = database.getReference("Users/${currentUserUid()}")
        databaseRef.setValue(user)
    }
}


