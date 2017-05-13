package com.asadmshah.livecolorpicker.models

import android.os.Parcel
import android.os.Parcelable

class ColorList: ArrayList<Color>(), Parcelable {
    companion object {
        @JvmField
        @Suppress("unused")
        val CREATOR = object : Parcelable.Creator<ColorList> {
            override fun createFromParcel(source: Parcel): ColorList {
                val colorList = ColorList()
                source.readTypedList(colorList, Color.CREATOR)
                return colorList
            }

            override fun newArray(size: Int): Array<ColorList> {
                return newArray(size)
            }
        }
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeTypedList(this)
    }

    override fun describeContents(): Int {
        return 0
    }
}
