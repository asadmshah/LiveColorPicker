package com.asadmshah.livecolorpicker.colors

import android.graphics.Bitmap
import android.support.v7.graphics.Palette
import com.asadmshah.livecolorpicker.models.Color
import com.asadmshah.livecolorpicker.models.ColorList
import com.asadmshah.livecolorpicker.models.ColorPalette
import com.asadmshah.livecolorpicker.storage.Storage
import io.reactivex.Observable
import io.reactivex.Single
import java.util.*

internal class ColorizerImpl constructor(val storage: Storage) : Colorizer {

    private val cached: Observable<Triple<String, RGBColor, LABColor>> = storage.colorsFileStream()
            .map {
                val split = it.split("\t")
                val name = split[0].trim()
                val code = android.graphics.Color.parseColor(split[1].trim())
                val rgb = RGBColor.create(code)
                val lab = LABColor.create(code)
                Triple(name, rgb, lab)
            }
            .cache()

    override fun map(rgb: Int): Single<Color> {
        val currentLab = LABColor.create(rgb)

        return cached
                .reduce { a, b ->
                    val diffa = a.third.distance(currentLab)
                    val diffb = b.third.distance(currentLab)
                    if (diffa < diffb) a else b
                }
                .toSingle()
                .map { (name, rgb) -> Color(name, rgb.toInt()) }
    }

    override fun palette(bitmap: Bitmap): Single<ColorPalette> {
        return Observable
                .fromCallable {
                    Palette.from(bitmap).generate().swatches
                }
                .flatMapIterable {
                    val mutable = it.toMutableList()
                    mutable.sortByDescending { it.hsl[2] }
                    mutable
                }
                .flatMap { map(it.rgb).toObservable() }
                .toList()
                .map { items -> ColorPalette(Date(), ColorList().apply { addAll(items) }) }
    }
}
