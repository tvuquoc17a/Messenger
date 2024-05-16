package com.example.messenger.viewmodel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.messenger.jwt.TokenManager
import com.example.messenger.model.LoginUser
import com.example.messenger.model.RegisterResponse
import com.example.messenger.model.SignUpUser
import com.example.messenger.model.User
import com.example.messenger.repository.UserRepository
import com.example.messenger.retrofit.RetrofitInstance
import com.example.messenger.retrofit.response.LoginResponse
import com.google.firebase.storage.FirebaseStorage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        var currentUser: User? = null
    }

    private val storage by lazy { FirebaseStorage.getInstance() }
    val errorNotification = MutableLiveData<String>()
    private var profileImageUrl = String()
    val loginStatus = MutableLiveData<Boolean>()
    val signUpStatus = MutableLiveData<Boolean>()
    val userRepository = UserRepository(application)
    private val retrofitInstance = RetrofitInstance(userRepository)


    fun loginUser(email: String, password: String): String? {
        val loginUser = LoginUser(email, password)
        retrofitInstance.api.login(loginUser).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.body()?.succeeded == true) {
                    val token = response.body()?.data?.token.toString()
                    Log.d(
                        "loginResponse",
                        "Token : $token\nName : ${TokenManager.getUserName(token)}\nId : ${TokenManager.getUserId(token)}\nAudience : ${TokenManager.getAudience(token)}\n"
                    )
                    userRepository.saveToken(token)
                    currentUser = User(
                        response.body()!!.data.user.id,
                        response.body()!!.data.user.name,
                        response.body()!!.data.user.avatarImageUrl
                    )
                    loginStatus.value = true
                } else {
                    Log.e("loginResponse", "Failed to login: ${response.body()?.succeeded}")
                    loginStatus.value = false
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("loginResponse", "Failure: ${t.message}")
            }
        })
        return null
    }

    fun currentUserId(): String {
        return currentUser?.id.toString()
    }

    suspend fun signUp(userName: String, password: String, name: String, selectedImageUri: Uri) {

        val signUpUser = SignUpUser(name, userName, password, getProfileUrl(selectedImageUri))
        retrofitInstance.api.createUser(signUpUser)
            .enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    if (response.body() != null && response.body()?.succeeded == true) {
                        Log.d("sign_up", "Success")
                    } else {
                        errorNotification.value = response.body()?.error?.message.toString()
                        response.errorBody()?.string()?.let {
                            try {
                                Log.e("sign_up", "Error Body: $it")
                            } catch (e: Exception) {
                                Log.e("sign_up", "Exception: $e")
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Log.e("sign_up", t.message.toString())
                }
            })
    }


    suspend fun getProfileUrl(uri: Uri): String {
        val fileName = UUID.randomUUID().toString()
        val ref = storage.getReference("/profile_images/${currentUserId()}/$fileName")
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

}


