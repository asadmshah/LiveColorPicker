package com.asadmshah.livecolorpicker.colors

internal data class RGBColor(val r: Int, val g: Int, val b: Int) {

    internal companion object {
        fun create(rgb: Int): RGBColor {
            val r = android.graphics.Color.red(rgb)
            val g = android.graphics.Color.green(rgb)
            val b = android.graphics.Color.blue(rgb)
            return RGBColor(r, g, b)
        }
    }

    fun toInt(): Int {
        return android.graphics.Color.rgb(r, g, b)
    }

}