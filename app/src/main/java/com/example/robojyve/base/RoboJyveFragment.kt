package com.example.robojyve.base

import android.transition.Fade
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.robojyve.RoboJyveMainActivity

/**
 * Base functionality for this application's fragments.
 * Use/extend this to add more UI functionality to fragments.
 */
abstract class RoboJyveFragment : Fragment() {
    /**
     * Set the title of the fragment
     */
    fun setTitle(string: String) {
        activity?.let { act ->
            if (act is AppCompatActivity)
                act.supportActionBar?.title = string
        } ?: Log.e(
            "navigation",
            "Activity is null still, try calling setTitle in or after your" +
                    "fragment's onActivityCreated method"
        )
    }

    /**
     * replace fragment via underlying activity's fragment manager
     */
    fun replaceFragment(
        fragment: Fragment,
        addToBackStack: Boolean,
        tag: String,
        fade: Fade? = null
    ) {
        callMainActivityFunction {
            it.replaceFragment(fragment, addToBackStack, tag, fade)
        }
    }

    private fun callMainActivityFunction(function: (RoboJyveMainActivity) -> Unit): Boolean {
        return if (activity is RoboJyveMainActivity) {
            function(activity as RoboJyveMainActivity)
            true
        } else {
            Log.e(
                "navigation",
                "function $function failed because activity is not voxermainactivity. " +
                        "actual = $activity"
            )
            false
        }
    }
}