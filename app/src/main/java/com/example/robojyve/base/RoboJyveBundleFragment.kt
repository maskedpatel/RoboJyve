package com.example.robojyve.base

import android.os.Bundle
import android.os.Parcelable

/**
 * Fragment optional implementation which handles lifecycle changes using onsavedinstancestate
 * and restoreinstancestate approach.
 */
abstract class RoboJyveBundleFragment<T : Parcelable> : RoboJyveFragment() {
    /**
     * Parcelable variable persisting through lifecycle changes
     */
    protected var parameters: T? = null

    /**
     * The key which subclasses of this class will assign to the parcelable to persist.
     * I.e. the field of the bundle you will create in getInstance() for your fragment
     */
    abstract fun onRestoreStateKey(): String

    /**
     * Create run at start of fragment
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initArgs(savedInstanceState)
    }

    /**
     * Initializes the arguments. overridable incase a subclass wants to do post processing
     * (i.e. saving the variables individually afterwards)
     */
    open fun initArgs(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState)
        } else {
            arguments?.let {
                parameters = it.getParcelable(onRestoreStateKey())
            }
        }
    }

    /**
     * Ran when mainactivity creates this fragment
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initArgs(savedInstanceState)
    }

    /**
     * In case of fragment being destroyed, saves necessary information before crashing to
     * reinitialize properly
     */
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(onRestoreStateKey(), parameters)
        super.onSaveInstanceState(outState)
    }

    private fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        parameters = savedInstanceState?.getParcelable(onRestoreStateKey())
    }

}