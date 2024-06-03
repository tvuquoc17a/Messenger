package com.example.messenger.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.messenger.R
import com.example.messenger.databinding.FragmentChatContentBinding
import com.example.messenger.databinding.FragmentChatsBinding
import com.example.messenger.databinding.ItemOnlineUserBinding
import com.example.messenger.model.User


class ChatContentFragment : Fragment() {
    private lateinit var binding: FragmentChatContentBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChatContentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = arguments?.getSerializable("user") as User
        binding.tvChatPartnerName.text = user.name
        loadImage(user.profileUrl,binding.imgChatContentUser)
        binding.toolBarChatContent.setNavigationOnClickListener{
            if(activity?.supportFragmentManager?.backStackEntryCount!! > 0){
                requireActivity().supportFragmentManager.popBackStack()
            }
            else{
                requireActivity().onBackPressed()
            }
        }
    }

    private fun loadImage(profileUrl: String, imgChatContentUser: ImageView) {
        Glide.with(this)
            .load(profileUrl)
            .into(imgChatContentUser)
    }

}