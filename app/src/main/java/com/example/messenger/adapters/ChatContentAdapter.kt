package com.example.messenger.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.messenger.R

class ChatContentAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == com.example.messenger.Constants.MESSAGE_SENT) {
            val view = layoutInflater.inflate(R.layout.message_send, parent, false)
            SentMessageViewHolder(view)
        } else {
            val view = layoutInflater.inflate(R.layout.message_receive, parent, false)
            ReceivedMessageViewHolder(view)
        }
    }

    inner class SentMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            // Bind data for sent message
        }
    }

    inner class ReceivedMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            // Bind data for received message
        }
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == com.example.messenger.Constants.MESSAGE_SENT) {
            (holder as SentMessageViewHolder).bind()
        } else {
            (holder as ReceivedMessageViewHolder).bind()
        }
    }
}