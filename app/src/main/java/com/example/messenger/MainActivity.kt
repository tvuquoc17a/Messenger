package com.example.messenger

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.messenger.databinding.ActivityMainBinding
import com.example.messenger.databinding.HeaderMenuBinding
import com.example.messenger.singleton.UserSingleton
import com.example.messenger.viewmodel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var headerMenuBinding: HeaderMenuBinding
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    val viewModel : MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        //enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        drawerLayout = findViewById(R.id.main)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = Navigation.findNavController(this, R.id.fragments)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        bottomNavigationView.selectedItemId = R.id.chatsFragment
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.chatsFragment -> {
                    navController.navigate(R.id.chatsFragment)
                    binding.tvTitle.text = "Chats"
                    true
                }

                R.id.callFragment -> {
                    navController.navigate(R.id.callFragment)
                    binding.tvTitle.text = "Calls"
                    true
                }

                R.id.peopleFragment -> {
                    navController.navigate(R.id.peopleFragment)
                    binding.tvTitle.text = "People"
                    true
                }

                R.id.storyFragment -> {
                    navController.navigate(R.id.storyFragment)
                    binding.tvTitle.text = "Stories"
                    true
                }

                else -> {
                    false
                }
            }
        }

        val drawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            binding.toolBarChats,
            R.string.open,
            R.string.close
        )
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        val headerView =  binding.navView.getHeaderView(0)
        headerMenuBinding = DataBindingUtil.bind(headerView)!!
        headerMenuBinding.userSingleton = UserSingleton
        headerMenuBinding.lifecycleOwner = this

    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

}