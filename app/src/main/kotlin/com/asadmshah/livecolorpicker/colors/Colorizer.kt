package com.asadmshah.livecolorpicker.colors

import io.reactivex.Single

interface Colorizer {

    fun map(rgb: Int): Single<Triple<String, String, Int>>

}