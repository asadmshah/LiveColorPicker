package com.asadmshah.livecolorpicker.database

import com.asadmshah.livecolorpicker.models.ColorPalette
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import java.util.*

interface ColorsStore {

    fun insert(colorPalette: ColorPalette): Single<ColorPalette>

    fun read(): Observable<ColorPalette>

    fun read(date: Date): Maybe<ColorPalette>

}