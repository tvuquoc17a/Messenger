package com.example.messenger.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.messenger.R
import com.example.messenger.retrofit.response.UserListItem
import com.example.messenger.viewmodel.OnItemClickListener

class OnlineUsersAdapter(val context: Context, var data: List<UserListItem>, private val listener: OnItemClickListener) : RecyclerView.Adapter<OnlineUsersAdapter.OnlineUsersViewHolder>() {
    inner class OnlineUsersViewHolder(itemView: View, private val listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }




        private val imageUserProfile: ImageView = itemView.findViewById(R.id.imgOnlineUser)
        private val userName: TextView = itemView.findViewById(R.id.tvUserName)
        fun onBind(user: UserListItem) {
            Log.d("OnlineUsersAdapter", "onBind: $user")

            Glide.with(context)
                .load(user.avatarImageUrl)
                .into(imageUserProfile)
            userName.text = user.name
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnlineUsersViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_online_user, parent, false)
        return OnlineUsersViewHolder(itemView, listener)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: OnlineUsersViewHolder, position: Int) {
        val user = data[position]
        Log.d("OnlineUsersAdapter", "onBindViewHolder - $position: $user")
        holder.onBind(user)
    }

    fun updateData(newData: List<UserListItem>) {

        data = newData

        notifyDataSetChanged()
    }

}