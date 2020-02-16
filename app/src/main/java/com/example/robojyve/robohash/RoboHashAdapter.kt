package com.example.robojyve.robohash

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.robojyve.R

/**
 * Adapter for discover recyclerview
 */
class RoboHashAdapter(val onRoboClickFn: (url: String) -> Unit) :
    RecyclerView.Adapter<RoboHashViewHolder>() {

    // Use list for O(1) read next runtime, most common use case
    private var roboItems: List<RoboHashItem> = listOf()
    /**
     * Create framework for discover viewholder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoboHashViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.robohash_recyclerview_item,
            parent,
            false
        )
        return RoboHashViewHolder(view, onRoboClickFn)
    }

    /**
     * Assign items for adapter
     */
    fun setRoboItems(items: List<RoboHashItem>) {
        roboItems = items
        notifyDataSetChanged()
    }

    /**
     * Bind data to the given viewholder
     */
    override fun onBindViewHolder(holder: RoboHashViewHolder, position: Int) {
        val currentItem = roboItems[position]
        holder.populateData(currentItem)
    }

    /**
     * Length of adapter items
     */
    override fun getItemCount(): Int {
        return roboItems.size
    }
}