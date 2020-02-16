package com.example.robojyve.robohash

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.robojyve.R
import com.example.robojyve.base.RoboJyveFragment

/**
 * Fragment for robo screen
 */
class RoboHashFragment: RoboJyveFragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RoboHashAdapter
    private lateinit var button: AppCompatButton
    private lateinit var roboUrl: AppCompatEditText

    private val sharedPreferences by lazy {
        requireContext().getSharedPreferences(
            roboPreferenceKey,
            Context.MODE_PRIVATE
        )
    }

    companion object {
        /**
         * Returns an instance of the robohashfragment
         */
        fun newInstance(): RoboHashFragment = RoboHashFragment()

        /**
         * Title of the fragment
         */
        const val fragmentTitle: String = "RoboHasher"
        /**
         * num columns in recycl view
         */
        const val numColumns: Int = 4
        /**
         * for shared preferences
         */
        const val savedUrlsKey: String = "saved_urls"
        /**
         * for shared preferences
         */
        const val roboPreferenceKey: String = "roboSP"
    }

    private lateinit var viewModel: RoboHashViewModel

    /**
     * View created
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.robohash_fragment, container, false)
    }

    /**
     * Initialize UI stuff here
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeRecyclerView(view)
        initializeUIItems(view)
    }

    private fun initializeUIItems(view: View) {
        button = view.findViewById(R.id.robobutton)
        roboUrl = view.findViewById(R.id.roborequest)

        button.setOnClickListener {
            val text = roboUrl.text.toString()
            if (text.isNotBlank()) {
                viewModel.addItem(RoboHashItem(text))
                addUrl(text)
            }
        }
    }

    /**
     * Initialize relevant data
     * This is done here in case information is needed from associated activity (i.e. title)
     * Viewmodel creation can potentially be done in onViewCreated as well, done here in case
     * future viewmodel needs data from activity to initialize
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setTitle(fragmentTitle)
        initializeViewModel()
    }

    /** retrieve current urls from shared pref **/
    private fun retrieveSavedUrls(pref: SharedPreferences = sharedPreferences): MutableSet<String> {
        // shouldnt need double bang since im passing default value... but whatever. java libraries :)
        return pref.getStringSet(savedUrlsKey, mutableSetOf())!!
    }

    /**
     * adds an url to shared preferences
     */
    private fun addUrl(url: String, pref: SharedPreferences = sharedPreferences) {
        val urls = retrieveSavedUrls()
        urls.add(url)
        val editor = pref.edit()
        editor.clear()
        editor.putStringSet(savedUrlsKey, urls).apply()
    }

    /**
     * Init recyclerview via grid layout
     */
    private fun initializeRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.roborecyclerview)
        recyclerView.layoutManager = GridLayoutManager(this.requireContext(), numColumns)
        adapter = RoboHashAdapter {
            addFragment(
                RoboHashFullScreenFragment.newInstance(it),
                true,
                RoboHashFullScreenFragment.fragmentTag
            )
        }
        recyclerView.adapter = adapter
    }

    /**
     * Init view model and observer for recyclerview data
     */
    private fun initializeViewModel() {
        viewModel = ViewModelProviders.of(this).get(RoboHashViewModel::class.java)
        val gifDataObserver = Observer<List<RoboHashItem>> { newData ->
            adapter.setRoboItems(newData)
        }
        viewModel.roboData.observe(this, gifDataObserver)
        val currentItems = retrieveSavedUrls().toMutableList()
        viewModel.updateData(currentItems)
    }
}
