package com.asadmshah.livecolorpicker.models

import android.os.Parcel
import android.os.Parcelable

data class Color(val name: String, val rgb: Int): Parcelable {
    companion object {
        @JvmField
        @Suppress("unused")
        val CREATOR = object : Parcelable.Creator<Color> {
            override fun createFromParcel(source: Parcel): Color {
                return Color(source.readString(), source.readInt())
            }

            override fun newArray(size: Int): Array<Color> {
                return newArray(size)
            }
        }
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
        dest.writeInt(rgb)
    }

    override fun describeContents(): Int {
        return 0
    }
}