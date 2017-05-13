package com.asadmshah.livecolorpicker.android

import android.os.Parcel
import android.os.Parcelable

fun Parcelable.marshall(): ByteArray {
    val parcel = Parcel.obtain()
    writeToParcel(parcel, 0)
    val bytes = parcel.marshall()
    parcel.recycle()
    return bytes
}

fun <T> Parcelable.Creator<T>.unmarshall(source: ByteArray): T {
    val parcel = Parcel.obtain()
    parcel.unmarshall(source, 0, source.size)
    parcel.setDataPosition(0)
    val result = createFromParcel(parcel)
    parcel.recycle()
    return result
}
