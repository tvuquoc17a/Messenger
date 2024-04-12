package com.example.messenger.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.messenger.databinding.ActivityLoginBinding
import com.example.messenger.viewmodel.AuthViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        showLoginButton()
        signUpButton()
    }

    private fun signUpButton() {
        binding.btnSignUp.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()


    }

    private fun showLoginButton() {
        binding.editTextLoginUserName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (binding.editTextLoginUserName.text.toString().isNotEmpty() || binding.editTextLoginPassWord.text.toString().isNotEmpty()) {
                    //button show with animation
                    binding.btnLogin.visibility = View.VISIBLE
                    binding.btnLogin.setOnClickListener() {
                        viewModel.loginUser(
                            binding.editTextLoginUserName.text.toString(),
                            binding.editTextLoginPassWord.text.toString()
                        )
                    }
                } else {
                    binding.btnLogin.visibility = View.GONE
                }
            }

        })
    }
}