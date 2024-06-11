package com.example.messenger.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import androidx.databinding.BindingAdapter
import de.hdodenhof.circleimageview.CircleImageView

@BindingAdapter("imageUrl")//tạo fun custom binding
fun bindImage(imgView : CircleImageView, imgUrl : String?){
    imgUrl?.let {
        Glide.with(imgView.context).load(imgUrl).into(imgView)
    }

}