package com.example.messenger.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.bumptech.glide.Glide
import com.example.messenger.ChatsActivity
import com.example.messenger.adapters.ChatContentAdapter
import com.example.messenger.databinding.FragmentChatContentBinding
import com.example.messenger.retrofit.response.UserListItem
import com.example.messenger.signalr.SignalRListener
import com.example.messenger.viewmodel.HomeViewModel
import kotlinx.coroutines.launch


class ChatContentFragment : Fragment() {
    private lateinit var binding: FragmentChatContentBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var user: UserListItem
    private var roomId: Int? = null
    //private lateinit var adapter: ChatContentAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as ChatsActivity).viewModel
        user = arguments?.getSerializable("user") as UserListItem

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatContentBinding.inflate(inflater, container, false)
        binding.rcvChatContent.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        setupToolBarBackButton()
        createChatRoom()

        return binding.root
    }

    private fun createChatRoom() {
        lifecycleScope.launch {
            roomId = viewModel.createRoom(user.id)
            Log.d("RoomId", "RoomId: ${roomId.toString()}")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(this).load(user.avatarImageUrl).into(binding.imgChatContentReceiver)
        binding.tvChatContentUserName.text = user.name
        performSendMessage()
    }

    private fun performSendMessage() {
        binding.btnSend.setOnClickListener {
            if (binding.editTextMessage.text?.isNotEmpty() == true && roomId != null) {
                viewModel.sendMessage(binding.editTextMessage.text.toString(), roomId!!)
                binding.editTextMessage.text!!.clear()
            }
        }
    }

    private fun setupToolBarBackButton() {
        val toolbar = binding.toolbarChatContent
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}