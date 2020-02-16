package com.example.robojyve.robohash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.example.network.ImageHandler
import com.example.robojyve.R
import com.example.robojyve.base.RoboJyveBundleFragment

class RoboHashFullScreenFragment: RoboJyveBundleFragment<RoboHashItem>() {

    private lateinit var title: AppCompatTextView
    private lateinit var roboImage: AppCompatImageView

    companion object {

        const val bundleKey: String = "robofullscreen"

        const val fragmentTag: String = "robofullscreenFragment"

        fun newInstance(url: String): RoboHashFullScreenFragment {
            val bundle = Bundle().apply {
                val item = RoboHashItem(url)
                putParcelable(bundleKey, item)
            }
            val fragment = RoboHashFullScreenFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onRestoreStateKey(): String {
        return bundleKey
    }

    /**
     * View created
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.robo_fullscreen, container, false)
    }

    /**
     * Initialize recyclerview after view created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeUIItems(view)
    }

    private fun initializeUIItems(view: View) {
        title = view.findViewById(R.id.fullscreen_title)
        roboImage = view.findViewById(R.id.fullscreen_robo)
        parameters?.url?.let { url ->
            title.text = url
            ImageHandler.loadImage(
                requireContext(),
                url,
                roboImage,
                R.drawable.ic_launcher_background
            )
        }
    }
}