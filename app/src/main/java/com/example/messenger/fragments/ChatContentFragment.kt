package com.example.messenger.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.messenger.MainActivity
import com.example.messenger.adapters.ChatContentAdapter
import com.example.messenger.databinding.FragmentChatContentBinding
import com.example.messenger.model.User
import com.example.messenger.repository.FirebaseUtil
import com.example.messenger.viewmodel.MainViewModel
import kotlinx.coroutines.launch


class ChatContentFragment : Fragment() {
    private lateinit var binding: FragmentChatContentBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var user: User
    private lateinit var roomId: String
    private lateinit var adapter : ChatContentAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        user = arguments?.getSerializable("user") as User
        lifecycleScope.launch {
            FirebaseUtil.auth.currentUser?.let {
                roomId = viewModel.getOrCreateRoom(it.uid, user.uid)
                viewModel.getMessagesInRoom(roomId)
                Log.d("ChatContentFragment", "onCreate: $roomId")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentChatContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rcvChatContent.layoutManager  = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val user = arguments?.getSerializable("user") as User
        binding.tvChatPartnerName.text = user.name
        loadImage(user.profileUrl, binding.imgChatContentUser)

        binding.toolBarChatContent.setNavigationOnClickListener {
            if (activity?.supportFragmentManager?.backStackEntryCount!! > 0) {
                requireActivity().supportFragmentManager.popBackStack()
            } else {
                requireActivity().onBackPressed()
            }
        }
        binding.btnSend.setOnClickListener{

            viewModel.sendMessage(roomId, binding.editTextChatContent.text.toString())
            binding.editTextChatContent.text?.clear()
        }
        adapter = ChatContentAdapter(requireContext(), emptyList())
        viewModel.messagesInRoom.observe(viewLifecycleOwner, Observer {
            adapter.messages = it
            adapter.notifyDataSetChanged()
            binding.rcvChatContent.scrollToPosition(adapter.itemCount)
        })
        binding.rcvChatContent.adapter = adapter

    }

    private fun loadImage(profileUrl: String, imgChatContentUser: ImageView) {
        Glide.with(this)
            .load(profileUrl)
            .into(imgChatContentUser)
    }

}