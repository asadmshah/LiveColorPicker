package com.asadmshah.livecolorpicker.screens.colors_list

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.asadmshah.livecolorpicker.R
import com.asadmshah.livecolorpicker.models.ColorList
import com.asadmshah.livecolorpicker.widgets.PaletteGridView
import java.text.DateFormat
import java.util.*

internal class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), ColorsListContract.ViewHolder {

    private val dateFormat: DateFormat = DateFormat.getDateTimeInstance()

    private val viewDate: TextView = itemView.findViewById(R.id.date) as TextView
    private val viewPaletteGrid: PaletteGridView = itemView.findViewById(R.id.palette_grid) as PaletteGridView

    override fun setDate(date: Date) {
        viewDate.text = dateFormat.format(date)
    }

    override fun setColorList(colorList: ColorList) {
        viewPaletteGrid.colorList = colorList
    }
}