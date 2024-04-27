package com.example.messenger.viewmodel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.messenger.jwt.TokenManager
import com.example.messenger.model.LoginUser
import com.example.messenger.model.SignUpUser
import com.example.messenger.repository.UserRepository
import com.example.messenger.retrofit.RetrofitInstance
import com.example.messenger.retrofit.response.LoginResponse
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AuthViewModel( application: Application) : AndroidViewModel(application) {

    private val storage by lazy { FirebaseStorage.getInstance() }
    val errorNotification = MutableLiveData<String>()
    private var profileImageUrl = String()
    val loginStatus = MutableLiveData<Boolean>()
    val userRepository = UserRepository(application)
    private val retrofitInstance = RetrofitInstance(userRepository)


    fun loginUser(email: String, password: String): String? {
        val loginUser = LoginUser(email, password)
        retrofitInstance.api.login(loginUser).enqueue(object : retrofit2.Callback<LoginResponse> {
            override fun onResponse(
                call: retrofit2.Call<LoginResponse>,
                response: retrofit2.Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    val token = response.body()?.data?.token.toString()
                    Log.d("loginResponse", "Token : $token\nName : ${TokenManager.getUserName(token)}\nId : ${TokenManager.getUserId(token)}\nAudience : ${TokenManager.getAudience(token)}\n")
                    userRepository.saveToken(token)
                    loginStatus.value = true
                }
            }

            override fun onFailure(call: retrofit2.Call<LoginResponse>, t: Throwable) {
                Log.e("loginResponse", "Failure: ${t.message}")
            }
        })
        return null
    }

    fun currentUserId(): String {
        return TokenManager.getUserId(userRepository.getToken().toString())
    }

    fun signUp(email: String, password: String, name: String) {
        val signUpUser = SignUpUser(name, email, password)
        retrofitInstance.api.createUser(signUpUser).enqueue(object : retrofit2.Callback<String> {
            override fun onResponse(
                call: retrofit2.Call<String>,
                response: retrofit2.Response<String>
            ) {
                if (response.isSuccessful) {
                    Log.d("sign_up", response.body().toString())
                }
            }

            override fun onFailure(call: retrofit2.Call<String>, t: Throwable) {
                Log.e("sign_up", t.message.toString())
            }
        })
    }


    suspend fun getProfileUrl(uri: Uri): String {
        val fileName = UUID.randomUUID().toString()
        val ref = storage.getReference("/profile_images/${currentUserId()}/$fileName")
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

}


