package com.example.messenger.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.example.messenger.databinding.ActivityLoginBinding
import com.example.messenger.viewmodel.AuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        loginButton()
        signUpButton()
        showProgressBar()
    }

    private fun showProgressBar() {
        viewModel.loginStatus.observe(this@LoginActivity) { isSuccess ->

            if (isSuccess) {
                binding.progressLoginLoading.visibility = View.GONE
                val intent = android.content.Intent(
                    this@LoginActivity,
                    com.example.messenger.ChatsActivity::class.java
                )
                startActivity(intent)
                finish()
            } else if (!isSuccess) {
                binding.progressLoginLoading.visibility = View.GONE
            }
        }
    }

    private fun signUpButton() {
        binding.btnSignUp.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
    }


    private fun loginButton() {
        binding.btnLogin.setOnClickListener() {
            viewModel.loginStatus.value = false
            binding.progressLoginLoading.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.Main).launch {
                val loginError = viewModel.loginUser(
                    binding.editTextLoginUserName.text.toString(),
                    binding.editTextLoginPassWord.text.toString()
                )
                if (loginError != null) {
                    binding.tvLoginError.visibility = View.VISIBLE
                    Log.d("login_error", "$loginError")
                    binding.tvLoginError.text = loginError.toString()
                }
                else {
                    binding.tvLoginError.visibility = View.GONE
                }
            }
        }
    }
}