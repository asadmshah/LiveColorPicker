package com.asadmshah.livecolorpicker.colors

import android.graphics.Bitmap
import io.reactivex.Observable
import io.reactivex.Single

interface Colorizer {

    fun map(rgb: Int): Single<Triple<String, String, Int>>

    fun palette(bitmap: Bitmap): Observable<Triple<String, String, Int>>

}