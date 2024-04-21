package com.example.messenger

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.messenger.databinding.ActivityChatsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class ChatsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatsBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = Navigation.findNavController(this, R.id.fragments)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        bottomNavigationView.selectedItemId = R.id.chatsFragment

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.chatsFragment -> {
                    navController.navigate(R.id.chatsFragment)
                    binding.toolBarChats.title = "Chats"
                    true
                }

                R.id.callFragment -> {
                    navController.navigate(R.id.callFragment)
                    binding.toolBarChats.title = "Calls"
                    true
                }

                R.id.peopleFragment -> {
                    navController.navigate(R.id.peopleFragment)
                    binding.toolBarChats.title = "People"
                    true
                }

                R.id.storyFragment -> {
                    navController.navigate(R.id.storyFragment)
                    binding.toolBarChats.title = "Stories"
                    true
                }

                else -> {
                    false
                }
            }
        }


    }

}