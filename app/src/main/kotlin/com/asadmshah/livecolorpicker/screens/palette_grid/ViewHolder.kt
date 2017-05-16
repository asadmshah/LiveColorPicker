package com.asadmshah.livecolorpicker.screens.palette_grid

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.asadmshah.livecolorpicker.R
import com.asadmshah.livecolorpicker.widgets.CircleView

internal class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), PaletteGridContract.ViewHolder {

    val viewCircle: CircleView = itemView.findViewById(R.id.color_circle) as CircleView
    val viewName: TextView = itemView.findViewById(R.id.color_name) as TextView
    val viewHex: TextView = itemView.findViewById(R.id.hex_value) as TextView
    val viewRGB: TextView = itemView.findViewById(R.id.rgb_value) as TextView

    override fun setColor(color: Int) {
        viewCircle.circleColor = color
    }

    override fun setName(name: String) {
        viewName.text = name
    }

    override fun setHex(hex: String) {
        viewHex.text = hex
    }

    override fun setRGB(rgb: String) {
        viewRGB.text = rgb
    }

}