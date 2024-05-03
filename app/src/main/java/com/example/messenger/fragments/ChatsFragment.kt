package com.example.messenger.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.messenger.ChatsActivity
import com.example.messenger.R
import com.example.messenger.adapters.HomeViewPagerAdapter
import com.example.messenger.adapters.OnlineUsersAdapter
import com.example.messenger.databinding.FragmentChatsBinding
import com.example.messenger.viewmodel.HomeViewModel
import com.example.messenger.viewmodel.OnItemClickListener
import com.google.android.material.tabs.TabLayoutMediator


class ChatsFragment : Fragment(), OnItemClickListener {
    private lateinit var binding: FragmentChatsBinding
    private lateinit var adapter: HomeViewPagerAdapter
    private lateinit var viewModel: HomeViewModel
    private lateinit var chatsViewPagerAdapter: HomeViewPagerAdapter
    private lateinit var onlineUsersAdapter: OnlineUsersAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        adapter = HomeViewPagerAdapter(childFragmentManager, lifecycle)

        viewModel = (activity as ChatsActivity).viewModel
        chatsViewPagerAdapter = HomeViewPagerAdapter(requireFragmentManager(), lifecycle)

        viewModel.getUserList()
        onlineUsersAdapter = OnlineUsersAdapter(requireContext(), emptyList(), this)
        viewModel.userLiveData.observe(this) {
            if (it != null) {
                onlineUsersAdapter.updateData(it)
            }
            onlineUsersAdapter = OnlineUsersAdapter(requireContext(), it, this)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.viewpagerChatUsers.adapter = adapter
        binding.viewpagerChatUsers.adapter = chatsViewPagerAdapter
        binding.rcvOnlineUser.adapter = onlineUsersAdapter

        TabLayoutMediator(
            binding.tabLayoutChatFragment,
            binding.viewpagerChatUsers
        ) { tab, position ->
            tab.text = when (position) {
                0 -> "Chats"
                1 -> "Channels"
                else -> "Invalid"
            }
        }.attach()

        binding.rcvOnlineUser.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    override fun onItemClick(position: Int) {
        val user = onlineUsersAdapter.data[position]
        val bundle = Bundle()
        bundle.putSerializable("user", user)
        val fragmentManager = activity?.supportFragmentManager!!
        val newFragment = ChatContentFragment()
        newFragment.arguments = bundle
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.main, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}