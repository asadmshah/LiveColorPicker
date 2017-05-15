package com.asadmshah.livecolorpicker.screens.colors_list

import android.os.Bundle
import com.asadmshah.livecolorpicker.models.ColorList
import java.util.*

interface ColorsListContract {

    interface View {

        fun enableList()

        fun notifyDataSetChanged()
    }

    interface Presenter {

        fun onCreate(savedInstanceState: Bundle?)

        fun onSaveInstanceState(outState: Bundle)

        fun onDestroy()

        fun getItemCount(): Int

        fun getItemId(position: Int): Long

        fun onBindViewHolder(viewHolder: ViewHolder, position: Int)

        fun onListItemClicked(position: Int)
    }

    interface ViewHolder {

        fun setDate(date: Date)

        fun setColorList(colorList: ColorList)
    }
}