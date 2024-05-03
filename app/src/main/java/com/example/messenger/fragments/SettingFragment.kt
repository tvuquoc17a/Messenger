package com.example.messenger.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.messenger.ChatsActivity
import com.example.messenger.R
import com.example.messenger.auth.LoginActivity
import com.example.messenger.databinding.FragmentSettingBinding
import com.example.messenger.viewmodel.AuthViewModel
import com.example.messenger.viewmodel.HomeViewModel


class SettingFragment : Fragment() {
    private lateinit var binding : FragmentSettingBinding
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as ChatsActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogout.setOnClickListener {
            viewModel.userRepository.clearToken()
            AuthViewModel.currentUser = null
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }


}