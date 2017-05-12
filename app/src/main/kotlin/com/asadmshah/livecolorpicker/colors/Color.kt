package com.asadmshah.livecolorpicker.colors

internal data class Color(val name: String, val rgb: RGBColor, val lab: LABColor) {

    internal companion object {
        fun create(name: String, hex: String): Color {
            val rgb = RGBColor.create(android.graphics.Color.parseColor(hex))
            val lab = LABColor.create(android.graphics.Color.parseColor(hex))
            return Color(name, rgb, lab)
        }
    }

}