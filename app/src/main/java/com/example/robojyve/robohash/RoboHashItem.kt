package com.example.robojyve.robohash

import android.os.Parcel
import android.os.Parcelable

/**
 * Item representing a robo hash item
 */
data class RoboHashItem(
    /**
     * url of robo hash
     */
    val url: String
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RoboHashItem> {
        override fun createFromParcel(parcel: Parcel): RoboHashItem {
            return RoboHashItem(parcel)
        }

        override fun newArray(size: Int): Array<RoboHashItem?> {
            return arrayOfNulls(size)
        }
    }
}