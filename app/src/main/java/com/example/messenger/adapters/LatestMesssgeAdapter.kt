package com.example.messenger.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.messenger.R
import com.example.messenger.model.Message
import com.example.messenger.singleton.UserSingleton

class LatestMessageAdapter(val context: Context, var latestMessageList : MutableList<Message> ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class LatestMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(message: Message){
            val tvLatestMessageContent = itemView.findViewById<TextView>(R.id.tvLatestChatContent)
            val imgPartner = itemView.findViewById<ImageView>(R.id.imgUserLatestMessage)
            val tvPartnerName = itemView.findViewById<TextView>(R.id.tvLatestPartnerName)
            tvLatestMessageContent.text = if(UserSingleton.user?.value?.uid   == message.senderId){
                "You : ${message.content}"
            }
            else{
                message.content
            }
            tvPartnerName.text = message.partnerName
            Glide.with(context).load(message.partnerImage).error(R.drawable.ic_image_not_supported_24).listener(object : RequestListener<Drawable>{
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    if (e != null) {
                        Log.e("LatestMessageAdapter", e.message!!)
                    }
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

            }).into(imgPartner)
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