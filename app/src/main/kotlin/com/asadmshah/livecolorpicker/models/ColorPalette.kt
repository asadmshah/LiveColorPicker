package com.asadmshah.livecolorpicker.models

import android.os.Parcel
import android.os.Parcelable
import java.util.*

data class ColorPalette(val date: Date, val colors: ColorList) : Parcelable {
    companion object {
        @JvmField
        @Suppress("unused")
        val CREATOR = object : Parcelable.Creator<ColorPalette> {
            override fun createFromParcel(source: Parcel): ColorPalette {
                val date = Date(source.readLong())
                val colors = source.readParcelable<ColorList>(ColorList::class.java.classLoader)
                return ColorPalette(date, colors)
            }

            override fun newArray(size: Int): Array<ColorPalette> {
                return newArray(size)
            }
        }
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(date.time)
        dest.writeParcelable(colors, flags)
    }

    override fun describeContents(): Int {
        return 0
    }
}