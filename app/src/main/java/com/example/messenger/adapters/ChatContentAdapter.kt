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
import com.example.messenger.model.Message
import com.example.messenger.singleton.UserSingleton

class ChatContentAdapter(val context: Context, var messages : List<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {



    companion object{
        private const val MESSAGE_SENT = 1
        private const val MESSAGE_RECEIVE = 2
    }

    override fun getItemViewType(position: Int): Int {
        val messages = messages[position]
        return if(UserSingleton.user?.value?.uid   == messages.senderId){
            MESSAGE_SENT
        }
        else MESSAGE_RECEIVE
    }
    inner class SentMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(message: Message){
            val tvMessageSent = itemView.findViewById<TextView>(R.id.tvMessageContentSend)
            tvMessageSent.text = message.content
        }
    }

    inner class ReceiveMessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(message: Message) {
            val tvMessageReceive = itemView.findViewById<TextView>(R.id.tvMessageContentReceive)
            val imgSender = itemView.findViewById<ImageView>(R.id.imgPartnerMessage)
            tvMessageReceive.text = message.content
            Glide.with(context).load(message.senderImage).into(imgSender)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == MESSAGE_SENT){
            val view = LayoutInflater.from(context).inflate(R.layout.item_message_sent, parent, false)
            SentMessageViewHolder(view)
        }
        else {
            val view = LayoutInflater.from(context).inflate(R.layout.item_message_receive, parent, false)
            ReceiveMessageHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d("ChatContentAdapter", "bind position $position")
        when(holder.itemViewType){
            MESSAGE_SENT -> (holder as SentMessageViewHolder).bind(messages[position])
            MESSAGE_RECEIVE -> (holder as ReceiveMessageHolder).bind(messages[position])
        }
    }
}