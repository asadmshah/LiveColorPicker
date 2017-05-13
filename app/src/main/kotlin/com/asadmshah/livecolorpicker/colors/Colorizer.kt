package com.asadmshah.livecolorpicker.colors

import android.graphics.Bitmap
import com.asadmshah.livecolorpicker.models.Color
import com.asadmshah.livecolorpicker.models.ColorPalette
import io.reactivex.Single

interface Colorizer {

    fun map(rgb: Int): Single<Color>

    fun palette(bitmap: Bitmap): Single<ColorPalette>

}