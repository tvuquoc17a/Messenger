package com.example.messenger.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.messenger.R
import com.example.messenger.databinding.ActivitySignInBinding
import com.example.messenger.model.User
import com.example.messenger.viewmodel.AuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var viewModel: AuthViewModel
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var selectedImageUri: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        chooseImageProfile()
        registerButton()
        backToLogin()


    }

    private fun backToLogin() {
        binding.tvReturnToLogin.setOnClickListener {
            onBackPressed()
        }
    }

    private fun chooseImageProfile() {
        binding.imgUserProfile.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            selectedImageUri = data?.data!!
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
            binding.imgUserProfile.setImageBitmap(bitmap)
        }
    }

    private fun registerButton() {
        binding.btnRegister.setOnClickListener {
            if (binding.editTextName.text.toString()
                    .isEmpty() || binding.editTextEmail.text.toString()
                    .isEmpty() || binding.editTextPassWord.text.toString().isEmpty()
            ) {
                Toast.makeText(this, "Please insert full information", Toast.LENGTH_SHORT).show()
            } else if (binding.editTextPassWord.text.toString() != binding.editTextRePassWord.text.toString()) {
                Toast.makeText(this, "Retype password doesn't match", Toast.LENGTH_SHORT).show()
            } else {

                val email = binding.editTextEmail.text.toString()
                val password = binding.editTextPassWord.text.toString()
                val name = binding.editTextName.text.toString()


                lifecycleScope.launch {
                    val profileUrlDeferred = async { viewModel.getProfileUrl(selectedImageUri) }
                    val profileUrl = profileUrlDeferred.await()
                    viewModel.signUp(email, password, name, profileUrl)
                    Log.d("sign_up", "Dispatchers.Main")
                }


                if (viewModel.errorNotification.isInitialized) {
                    binding.tvRegistrationStatus.apply {
                        text = viewModel.errorNotification.value.toString()
                        visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}