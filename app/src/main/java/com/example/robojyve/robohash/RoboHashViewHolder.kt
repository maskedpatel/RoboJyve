package com.example.robojyve.robohash

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.network.ImageHandler
import com.example.robojyve.R

/**
 * View holder visualizing robo pic
 */
class RoboHashViewHolder(
    itemView: View,
    val imageClickFn: (url: String) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        const val urlBase: String = "https://robohash.org/"
    }

    private val thumbnail: AppCompatImageView = itemView.findViewById(R.id.roboimage)

    /**
     * Populate this viewholder given a robo item.
     * Note we use a data class here incase we want to add features to this view holder (i.e.
     * more views etc) i.e. abstraction
     */
    fun populateData(roboHashItem: RoboHashItem) {
        //happens in IO thread async

        val url = urlBase + roboHashItem.url
        ImageHandler.loadImage(
            itemView.context,
            url,
            thumbnail,
            R.drawable.ic_launcher_background
        )
        thumbnail.setOnClickListener {
            imageClickFn(url)
        }
    }
}