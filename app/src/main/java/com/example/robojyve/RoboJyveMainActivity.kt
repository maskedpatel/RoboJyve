package com.example.robojyve

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode.VmPolicy
import android.os.StrictMode
import android.transition.Fade
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.robojyve.robohash.RoboHashFragment


/**
 * Underlying activity used for doordash lite app.
 * Following single activity - multiple fragment architecture
 * Allows targeting of multiple screens and acts as a fragment controller
 */
class RoboJyveMainActivity : AppCompatActivity() {

    /**
     * Doordash main activity create
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build()
            )
            StrictMode.setVmPolicy(
                VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build()
            )
        }
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_activity, RoboHashFragment.newInstance())
                .commitNow()
        }
    }

    /**
     * Replace Replace current showing fragment with inputted fragment
     */
    fun replaceFragment(
        fragment: Fragment,
        addToBackStack: Boolean,
        tag: String,
        fade: Fade?,
        manager: FragmentManager = supportFragmentManager
    ) {
        val ft = manager.beginTransaction()
        fragment.enterTransition = fade
        if (addToBackStack) {
            ft.addToBackStack(tag)
        }
        ft.replace(R.id.main_activity, fragment, tag)
        ft.commitAllowingStateLoss()
    }
}
