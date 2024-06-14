package com.example.messenger

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.messenger.databinding.ActivityMainBinding
import com.example.messenger.databinding.HeaderMenuBinding
import com.example.messenger.fragments.CallFragment
import com.example.messenger.fragments.ChatsFragment
import com.example.messenger.fragments.PeopleFragment
import com.example.messenger.fragments.StoryFragment
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
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)

       if(savedInstanceState == null){
           supportFragmentManager.beginTransaction()
               .replace(R.id.fragments, ChatsFragment())
               .commit()
       }

        val chatsFragment = ChatsFragment()
        val peopleFragment = PeopleFragment()
        val storyFragment = StoryFragment()
        val callFragment = CallFragment()

        //setCurrentFragment(chatsFragment)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.chatsFragment -> setCurrentFragment(chatsFragment)
                R.id.storyFragment -> setCurrentFragment(storyFragment)
                R.id.peopleFragment -> setCurrentFragment(peopleFragment)
                R.id.callFragment -> setCurrentFragment(callFragment)
            }
            true
        }

        /*bottomNavigationView.setOnNavigationItemReselectedListener {
            when (it.itemId) {
                R.id.chatsFragment -> setCurrentFragment(chatsFragment)
                R.id.peopleFragment -> setCurrentFragment(peopleFragment)
                R.id.storyFragment -> setCurrentFragment(storyFragment)
                R.id.callFragment -> setCurrentFragment(callFragment)
            }
        }*/

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

    private fun setCurrentFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.fragments, fragment).commit()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

}