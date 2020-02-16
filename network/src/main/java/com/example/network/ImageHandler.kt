package com.example.network

import android.content.Context
import android.net.Uri
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

/**
 * Image handling object
 */
object ImageHandler {
    /**
     * Given url, will retrieve and load image into given imageview
     * Note that the glide call automatically occurs in IO thread asynchronously
     * Caching is in place, so you may see memory use gradually go up in profiler,
     * but eventually it will go back down.
     * @param context Context to associate call to
     * @param url url of image
     * @param imageView image view to load image into
     * @param placeHolderDrawable placeholder incase call fails
     */
    fun loadImage(
        context: Context,
        url: String,
        imageView: AppCompatImageView,
        placeHolderDrawable: Int
    ) {
        // by default will run async in IO thread
        Glide.with(context).clear(imageView)
        Glide.with(context)
            .load(Uri.parse(url))
            .placeholder(placeHolderDrawable)
            .fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imageView)
    }
}