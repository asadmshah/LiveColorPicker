package com.asadmshah.livecolorpicker.colors

import android.support.v4.graphics.ColorUtils

internal data class LABColor(val l: Double, val a: Double, val b: Double) {

    internal companion object {
        fun create(rgb: Int): LABColor {
            val (r, g, b) = RGBColor.create(rgb)
            val outs = doubleArrayOf(0.0, 0.0, 0.0)
            ColorUtils.RGBToLAB(r, g, b, outs)
            return LABColor(outs[0], outs[1], outs[2])
        }
    }

    internal fun distance(other: LABColor): Double {
        return Math.sqrt(Math.pow(l - other.l, 2.0) + Math.pow(a - other.a, 2.0) + Math.pow(b - other.b, 2.0))
    }

}