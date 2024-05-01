package com.example.messenger.fragments
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.messenger.ChatsActivity
import com.example.messenger.adapters.HomeViewPagerAdapter
import com.example.messenger.adapters.OnlineUsersAdapter
import com.example.messenger.databinding.FragmentChatsBinding
import com.example.messenger.viewmodel.HomeViewModel
import com.google.android.material.tabs.TabLayoutMediator


class ChatsFragment : Fragment() {
    private lateinit var binding : FragmentChatsBinding
    private lateinit var adapter : HomeViewPagerAdapter
    private lateinit var viewModel: HomeViewModel
    private lateinit var chatsViewPagerAdapter : HomeViewPagerAdapter
    private lateinit var onlineUsersAdapter: OnlineUsersAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        adapter = HomeViewPagerAdapter(childFragmentManager,lifecycle)

        viewModel = (activity as ChatsActivity).viewModel
        chatsViewPagerAdapter = HomeViewPagerAdapter(requireFragmentManager(),lifecycle)

        viewModel.getUserList()
        onlineUsersAdapter = OnlineUsersAdapter(requireContext(), emptyList())
        viewModel.userLiveData.observe(this) {
            if(it != null){
                onlineUsersAdapter.updateData(it)
            }
            onlineUsersAdapter = OnlineUsersAdapter(requireContext(), it)
        }



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.viewpagerChatUsers.adapter = adapter

        binding.viewpagerChatUsers.adapter = chatsViewPagerAdapter
        binding.rcvOnlineUser.adapter = onlineUsersAdapter

        TabLayoutMediator(binding.tabLayoutChatFragment,binding.viewpagerChatUsers){tab,position ->
            tab.text = when(position){
                0 -> "Chats"
                1 -> "Channels"
                else -> "Invalid"
            }
        }.attach()

        binding.rcvOnlineUser.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)


    }
}