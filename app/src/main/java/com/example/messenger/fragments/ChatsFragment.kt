package com.example.messenger.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.registerForActivityResult
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.messenger.MainActivity
import com.example.messenger.R
import com.example.messenger.adapters.OnItemClickListener
import com.example.messenger.adapters.OnlineUserAdapter

import com.example.messenger.adapters.ViewPagerAdapter2
import com.example.messenger.databinding.FragmentChatsBinding
import com.example.messenger.singleton.UserSingleton
import com.example.messenger.viewmodel.MainViewModel
import com.google.android.material.tabs.TabLayoutMediator


class ChatsFragment : Fragment() {
    private lateinit var binding: FragmentChatsBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: OnlineUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        adapter = OnlineUserAdapter(
            requireContext(),
            viewModel.onlineUserList,
            object : OnItemClickListener {
                override fun onItemClick(position: Int) {
                    val user = viewModel.onlineUserList.value?.get(position)
                    val bundle = Bundle()
                    val fragmentManager = activity?.supportFragmentManager!!
                    val newFragment = ChatContentFragment()
                    val transaction = fragmentManager.beginTransaction()
                    transaction.setCustomAnimations(
                        android.R.anim.slide_in_left,  // hiệu ứng khi fragment mới xuất hiện
                        android.R.anim.slide_out_right, // hiệu ứng khi fragment cũ biến mất
                        android.R.anim.slide_in_left,  // hiệu ứng khi fragment cũ quay lại
                        android.R.anim.slide_out_right  // hiệu ứng khi fragment mới biến mất
                    )
                    newFragment.arguments = bundle
                    bundle.putSerializable("user", user)
                    transaction.replace(R.id.main, newFragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                }
            })
        UserSingleton.user?.value?.let { Log.d("user_id", it.uid) }
        Log.d("ChatFragment", "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatsBinding.inflate(inflater, container, false)
        binding.rcvOnlineUsers.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("ChatFragment", "onViewCreated")
        viewModel.fetchOnlineUsers()
        binding.rcvOnlineUsers.adapter = adapter
        binding.viewPagerLatestMessage.adapter = ViewPagerAdapter2(requireActivity())
        viewModel.onlineUserList.observe(viewLifecycleOwner) {
            adapter.notifyDataSetChanged()
        }

        binding.viewPagerLatestMessage.offscreenPageLimit = 2
        TabLayoutMediator(
            binding.tabLayoutChatFragment,
            binding.viewPagerLatestMessage
        ) { tab, position ->
            when (position) {
                0 -> tab.text = "Home"
                1 -> tab.text = "Chanel"
            }
        }.attach()
        binding.viewPagerLatestMessage.setCurrentItem(0, true)

    }

}