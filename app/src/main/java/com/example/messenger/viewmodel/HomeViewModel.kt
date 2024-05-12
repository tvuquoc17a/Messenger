package com.example.messenger.viewmodel

import android.app.Application
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.example.messenger.R
import com.example.messenger.fragments.SettingFragment
import com.example.messenger.jwt.TokenManager
import com.example.messenger.repository.UserRepository
import com.example.messenger.retrofit.RetrofitInstance
import com.example.messenger.retrofit.request.RoomInfo
import com.example.messenger.retrofit.response.UserListItem
import com.example.messenger.signalr.SignalRListener
import com.example.messenger.signalr.SignalrMessage
import com.google.android.material.navigation.NavigationView
import com.microsoft.signalr.HubConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    var userRepository = UserRepository(application)
    private val retrofitInstance = RetrofitInstance(userRepository)
    val userLiveData = MutableLiveData<List<UserListItem>>()
    private var signalRListener: SignalRListener = SignalRListener.getInstance(userRepository.getToken())

    fun setupHeaderOfNavigationDrawer(
        navigationView: NavigationView,
        fragmentManager: FragmentManager
    ) {

        val headerView = navigationView.getHeaderView(0)
        val userImage = headerView.findViewById<ImageView>(R.id.imgUserProfile)
        val userName = headerView.findViewById<TextView>(R.id.tvHeaderUserName)
        val buttonSetting = headerView.findViewById<ImageView>(R.id.iconSetting)
        Log.d("image", AuthViewModel.currentUser?.profileImageUrl.toString())
        Glide.with(navigationView.context)
            .load(AuthViewModel.currentUser?.profileImageUrl)
            .into(userImage)

        userName.text = AuthViewModel.currentUser?.name.toString()
        buttonSetting.setOnClickListener {
            Log.d("HomeViewModel", "Setting button clicked")
            val newFragment = SettingFragment()
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.main, newFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    fun getUserList() {
        retrofitInstance.api.getUserList().enqueue(object : Callback<List<UserListItem>> {
            override fun onResponse(
                call: Call<List<UserListItem>>,
                response: Response<List<UserListItem>>
            ) {
                if (response.isSuccessful) {

                    userLiveData.value = response.body()
                    userLiveData.value?.let {
                        for (user in it) {
                            Log.d(
                                "getUserList",
                                "User id: ${user.id}, account: ${user.account}, name: ${user.name}, avatarImageUrl: ${user.avatarImageUrl}"
                            )
                        }
                    }
                } else {
                    //log error
                    response.errorBody().let { errorBody ->
                        Log.d("getUserList", "Failed to get user list : ${errorBody.toString()}")
                    }
                }
            }

            override fun onFailure(call: Call<List<UserListItem>>, t: Throwable) {
                Log.d("getUserList", "Failed to get user list : ${t.message}")
            }
        })
    }

    suspend fun createRoom(receiverId: Int): Int? {
        val roomInfo = RoomInfo(AuthViewModel.currentUser!!.id, receiverId)
        return try {
            val response = withContext(Dispatchers.IO) {
                retrofitInstance.api.createRoom(roomInfo).execute()
            }
            if (response.isSuccessful) {
                Log.d("createRoom", response.body().toString())
                response.body()?.data?.roomDot?.id

            } else {
                response.errorBody().let { errorBody ->
                    Log.d("createRoom", "Failed to create room : ${errorBody.toString()}")
                }
                null
            }
        } catch (e: Exception) {
            Log.d("createRoom", "Failed to create room : ${e.message}")
            null
        }
    }

    fun sendMessage(content: String, roomId: Int) {
        val message = SignalrMessage(
            roomId,
            content,
            AuthViewModel.currentUser!!.id,
            AuthViewModel.currentUser!!.name
        )
        Log.d("sendMessage", "${signalRListener.connectionState}")
        if (signalRListener.startConnection()) {
            signalRListener.sendMessage(message, roomId)
        }

    }
}