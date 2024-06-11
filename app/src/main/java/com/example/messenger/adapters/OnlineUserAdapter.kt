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


    inner class OnlineUserViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        fun onBind(user: User) {
            val imgOnlineUser = itemView.findViewById<ImageView>(R.id.imgOnlineUser)
            val tvOnLineUserName = itemView.findViewById<TextView>(R.id.tvOnlineUserName)
            Glide.with(context).load(user.profileUrl).into(imgOnlineUser)
            tvOnLineUserName.text = user.name

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnlineUserViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_online_user,parent, false)
        return OnlineUserViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        Log.d("OnlineUserAdapter", "rcv size : ${data.value?.size}")
        return data.value?.size ?: 0
    }

    override fun onBindViewHolder(holder: OnlineUserViewHolder, position: Int) {
        holder.onBind(data.value!![position])
        holder.itemView.setOnClickListener{ listener?.onItemClick(position) }
        Log.d("OnlineUserAdapter", "onBindViewHolder: $position")
    }
}