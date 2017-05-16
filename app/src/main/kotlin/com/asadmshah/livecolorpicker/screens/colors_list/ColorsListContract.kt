package com.asadmshah.livecolorpicker.screens.colors_list

import android.os.Bundle
import com.asadmshah.livecolorpicker.models.ColorList
import com.asadmshah.livecolorpicker.models.ColorPalette
import java.util.*

interface ColorsListContract {

    interface View {

        fun navigateToPaletteGridScreen(colorPalette: ColorPalette)

        fun navigateUp()

        fun enableList()

        fun notifyDataSetChanged()

        fun setProgressBarEnabled(enabled: Boolean)
    }

    interface Presenter {

        fun onCreate(savedInstanceState: Bundle?)

        fun onSaveInstanceState(outState: Bundle)

        fun onDestroy()

        fun getItemCount(): Int

        fun getItemId(position: Int): Long

        fun onBindViewHolder(viewHolder: ViewHolder, position: Int)

        fun onListItemClicked(position: Int)

        fun onNavigationButtonClicked()

        fun onPullToRefresh()
    }

    interface ViewHolder {

        fun setDate(date: Date)

        fun setColorList(colorList: ColorList)
    }
}