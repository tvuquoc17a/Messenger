package com.example.messenger.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.messenger.R
import com.example.messenger.model.Message

class LatestMessageAdapter(val context: Context, var latestMessageList : MutableList<Message> ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class LatestMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(message: Message){
            val tvLatestMessageContent = itemView.findViewById<TextView>(R.id.tvLatestChatContent)
            val imgPartner = itemView.findViewById<ImageView>(R.id.imgPartnerMessage)
            val tvPartnerName = itemView.findViewById<TextView>(R.id.tvChatPartnerName)
            tvLatestMessageContent.text = message.content
            
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_latest_message, parent, false)
        return LatestMessageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return latestMessageList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as LatestMessageViewHolder).bind(latestMessageList[position])
    }
}