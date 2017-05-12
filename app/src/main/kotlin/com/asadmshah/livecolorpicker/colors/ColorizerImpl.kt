package com.asadmshah.livecolorpicker.colors

import android.content.Context
import com.asadmshah.livecolorpicker.R
import io.reactivex.Observable
import io.reactivex.Single

class ColorizerImpl constructor(val context: Context) : Colorizer {

    private val cached: Observable<Color> = Observable
            .create<Color> { emitter ->
                context.resources.openRawResource(R.raw.colors).use {
                    it.bufferedReader().useLines { sequence ->
                        sequence.filterIndexed { i, _ -> i > 0 }
                                .map {
                                    val split = it.split("\t")
                                    val name = split[0].trim()
                                    val hex = split[1].trim()
                                    Color.create(name, hex)
                                }
                                .forEach {
                                    emitter.onNext(it)
                                }
                    }
                }
                emitter.onComplete()
            }
            .cache()

    override fun map(rgb: Int): Single<Triple<String, String, Int>> {
        val currentLab = LABColor.create(rgb)

        return cached
                .reduce { a: Color, b: Color ->
                    val diffa = a.lab.distance(currentLab)
                    val diffb = b.lab.distance(currentLab)
                    if (diffa < diffb) a else b
                }
                .toSingle()
                .map { Triple(it.name, it.rgb.toHex(), it.rgb.toInt()) }
    }

}