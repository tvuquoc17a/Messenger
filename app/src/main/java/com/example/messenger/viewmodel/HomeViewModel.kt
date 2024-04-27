package com.example.messenger.viewmodel

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.example.messenger.R
import com.google.android.material.navigation.NavigationView

class HomeViewModel : ViewModel() {


    fun setupHeaderOfNavigationDrawer(navigationView: NavigationView) {

        val headerView = navigationView.getHeaderView(0)
        val userImage = headerView.findViewById<ImageView>(R.id.imgUserProfile)
        val userName = headerView.findViewById<TextView>(R.id.tvHeaderUserName)
        Log.d("image", AuthViewModel.currentUser?.profileImageUrl.toString())
        Glide.with(navigationView.context)
            .load(AuthViewModel.currentUser?.profileImageUrl)
            .into(userImage)

        userName.text = AuthViewModel.currentUser?.name.toString()
    }
}