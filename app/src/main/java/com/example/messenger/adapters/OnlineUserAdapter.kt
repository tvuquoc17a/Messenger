package com.example.messenger.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.messenger.R
import com.example.messenger.databinding.ItemOnlineUserBinding
import com.example.messenger.model.User

class OnlineUserAdapter(val context: Context, private val data: LiveData<List<User>>,private val listener : OnItemClickListener? = null) :
    RecyclerView.Adapter<OnlineUserAdapter.OnlineUserViewHolder>() {


    inner class OnlineUserViewHolder(val binding: ItemOnlineUserBinding) : RecyclerView.ViewHolder(binding.root) {
        private val userImage: ImageView = itemView.findViewById(R.id.imgOnlineUser)
        private val userName: TextView = itemView.findViewById(R.id.tvOnlineUserName)
        fun onBind(user: User) {
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnlineUserViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = ItemOnlineUserBinding.inflate(inflater, parent, false)
        return OnlineUserViewHolder(binding)
    }

    override fun getItemCount(): Int {
        Log.d("OnlineUserAdapter", "rcv size : ${data.value?.size}")
        return data.value?.size ?: 0
    }

    override fun onBindViewHolder(holder: OnlineUserViewHolder, position: Int) {
        holder.onBind(data.value!![position])
        val user = data.value!![position]
        holder.binding.user = user
        holder.itemView.setOnClickListener{ listener?.onItemClick(position) }
        Log.d("OnlineUserAdapter", "onBindViewHolder: $position")
    }
}