package com.example.messenger.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.messenger.ChatsActivity
import com.example.messenger.R
import com.example.messenger.databinding.FragmentChatContentBinding
import com.example.messenger.retrofit.response.UserListItem
import com.example.messenger.viewmodel.HomeViewModel


class ChatContentFragment : Fragment() {
    private lateinit var binding : FragmentChatContentBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var user : UserListItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as ChatsActivity).viewModel
        user = arguments?.getSerializable("user") as UserListItem
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatContentBinding.inflate(inflater,container,false)
        setupToolBarBackButton()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(this).load(user.avatarImageUrl).into(binding.imgChatContentReceiver)
        binding.tvChatContentUserName.text = user.name
    }

    private fun setupToolBarBackButton() {
        val toolbar = binding.toolbarChatContent
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener{
            requireActivity().onBackPressed()
        }
    }
}