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
        adapter = LatestMessageAdapter(requireContext(), mutableListOf())
        binding.rcvLatestMessages.layoutManager  = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)

        viewModel.latestMessageList.observe(viewLifecycleOwner, Observer {
            adapter.latestMessageList = it
            adapter.notifyDataSetChanged()
            Log.d("latestMessageList", "$it")
        })
        binding.rcvLatestMessages.adapter = adapter
    }

}