package com.asadmshah.livecolorpicker.colors

import android.content.Context
import android.graphics.Bitmap
import android.support.v7.graphics.Palette
import com.asadmshah.livecolorpicker.R
import com.asadmshah.livecolorpicker.models.Color
import com.asadmshah.livecolorpicker.models.ColorList
import com.asadmshah.livecolorpicker.models.ColorPalette
import io.reactivex.Observable
import io.reactivex.Single
import java.util.*

class ColorizerImpl constructor(val context: Context) : Colorizer {

    private val cached: Observable<Triple<String, RGBColor, LABColor>> = Observable
            .create<Triple<String, RGBColor, LABColor>> { emitter ->
                context.resources.openRawResource(R.raw.colors).use {
                    it.bufferedReader().useLines { sequence ->
                        sequence.filterIndexed { i, _ -> i > 0 }
                                .map {
                                    val split = it.split("\t")
                                    val name = split[0].trim()
                                    val code = android.graphics.Color.parseColor(split[1].trim())
                                    val rgb = RGBColor.create(code)
                                    val lab = LABColor.create(code)
                                    Triple(name, rgb, lab)

                                }
                                .forEach {
                                    emitter.onNext(it)
                                }
                    }
                }
                emitter.onComplete()
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
                .flatMapIterable { it.sortByDescending { it.hsl[2] }; it }
                .flatMap { map(it.rgb).toObservable() }
                .toList()
                .map { items -> ColorPalette(Date(), ColorList().apply { addAll(items) }) }
    }
}
