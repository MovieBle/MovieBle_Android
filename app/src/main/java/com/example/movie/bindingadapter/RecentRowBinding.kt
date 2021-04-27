package com.example.movie.bindingadapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.movie.untils.App

class RecentRowBinding {

    companion object {
        @BindingAdapter("recentImageUrl")
        @JvmStatic
        fun recentImageUrl(imageView: ImageView, imageUrl: String) {
            Glide.with(imageView.context).load(imageUrl).into(imageView)
        }
    }
}