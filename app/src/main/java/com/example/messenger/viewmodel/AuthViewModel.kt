package com.example.messenger.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.messenger.model.User
import com.example.messenger.repository.FirebaseUtil
import com.example.messenger.singleton.UserSingleton
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AuthViewModel : ViewModel() {
    private val auth = FirebaseUtil.auth
    private val database = FirebaseUtil.database
    private lateinit var currentUser: FirebaseUser
    val errorNotification = MutableLiveData<String>()
    private var profileImageUrl = String()
    val loginStatus = MutableLiveData<Boolean>()

    suspend fun loginUser(email: String, password: String): String? {
        return suspendCoroutine { continuation ->
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        loginStatus.value = true
                        currentUser = auth.currentUser!!
                        val userReference = database.getReference("/Users/${currentUser.uid}")
                        userReference.addListenerForSingleValueEvent(userListener)
                        continuation.resume(null)
                    }
                }.addOnFailureListener {
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


    fun signUp(email: String, password: String, name: String, profileUrl: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                currentUser = auth.currentUser!!
                addUserToDataBase(name, email, profileUrl)
                Log.d("sign_up", "success")
            }
            .addOnFailureListener {
                if (it is FirebaseAuthUserCollisionException) {
                    errorNotification.value = "Email already in use"
                } else {
                    Log.d("sign_up", "failed")
                }
            }

    }


    suspend fun getProfileUrl(uri: Uri): String {
        val fileName = UUID.randomUUID().toString()
        val ref = FirebaseUtil.storage.getReference("/profile_images/$fileName")
        return suspendCoroutine { continuation ->
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
        val user = User(currentUser.uid, name, email, profileUrl)
        database.reference.child("Users").child(currentUser.uid).setValue(user)
    }
    private val userListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            UserSingleton.user?.value = snapshot.getValue(User::class.java)
            Log.d("login_button", UserSingleton.user?.value.toString())
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d("user_listener", "error")
        }
    }
}


