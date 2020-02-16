package com.example.robojyve.robohash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * View model for robo data
 */
class RoboHashViewModel : ViewModel() {

    /**
     * Livedata of items corresponding to robo hash urls
     */
    val roboData: MutableLiveData<MutableList<RoboHashItem>> by lazy {
        MutableLiveData<MutableList<RoboHashItem>>()
    }

    private fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }

    fun updateData(items: MutableList<String>) {
        roboData.postValue(
            items.map {
                RoboHashItem(it)
            }.toMutableList()
        )
    }

    fun addItem(item: RoboHashItem) {
        roboData.value?.add(0, item)
        roboData.notifyObserver()
    }
}
