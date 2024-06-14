package com.example.messenger.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.messenger.MainActivity
import com.example.messenger.R
import com.example.messenger.adapters.LatestMessageAdapter
import com.example.messenger.adapters.OnItemClickListener
import com.example.messenger.databinding.FragmentLatestMessageBinding
import com.example.messenger.databinding.HeaderMenuBinding
import com.example.messenger.singleton.UserSingleton
import com.example.messenger.viewmodel.MainViewModel


class LatestMessageFragment : Fragment() {
    private lateinit var binding: FragmentLatestMessageBinding
    private lateinit var viewModel : MainViewModel
    private lateinit var adapter : LatestMessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        UserSingleton.user?.value?.let { viewModel.getLatestMessage(it.uid) }
        Log.d("LatestMessageFragment", "created")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLatestMessageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("LatestMessageFragment", "onViewCreated")
        adapter = LatestMessageAdapter(requireContext(), mutableListOf(), object : OnItemClickListener {
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
        binding.rcvLatestMessages.layoutManager  = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
        binding.rcvLatestMessages.adapter = adapter
        viewModel.latestMessageList.observe(viewLifecycleOwner, Observer {
            adapter.latestMessageList = it
            adapter.notifyDataSetChanged()
            Log.d("latestMessageList", "$it")
        })

    }

}