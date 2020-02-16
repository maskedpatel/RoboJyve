package com.example.robojyve.dagger

import com.example.robojyve.robohash.RoboHashAdapter
import com.example.robojyve.robohash.RoboHashFragment
import dagger.Component

/**
 * Dagger component to build DAG
 */
@Component
interface RoboHashAdapterComponent {
    /**
     * retrieves adapter
     */
    fun getAdapter(): RoboHashAdapter

    /**
     * inject
     */
    fun inject(fragment: RoboHashFragment)
}