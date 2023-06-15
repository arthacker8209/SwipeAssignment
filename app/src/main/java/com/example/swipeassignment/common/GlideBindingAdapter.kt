package com.example.swipeassignment.common

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

class GlideBindingAdapter {
    companion object{
        @SuppressLint("CheckResult")
        @JvmStatic
        @BindingAdapter("imageUrl", requireAll = false)
        fun setImageWithPlaceholder(
            imageView: ImageView,
            imageUrl: String,
        ) {
            val requestManager = Glide.with(imageView.context).load(imageUrl)
                .apply(if (imageUrl == null) RequestOptions().fitCenter() else RequestOptions().centerCrop())
                .transition(
                    DrawableTransitionOptions.withCrossFade()
                )
            requestManager.into(imageView)
        }
    }
}