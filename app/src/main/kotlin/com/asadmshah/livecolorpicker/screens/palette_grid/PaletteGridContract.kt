package com.asadmshah.livecolorpicker.screens.palette_grid

import android.os.Bundle
import android.support.annotation.ColorInt

interface PaletteGridContract {

    interface View {

        fun navigateUp()

        fun enableList()

        fun notifyDataSetChanged()

        fun setProgressBarEnabled(enabled: Boolean)

        fun clipboardPut(key: String, value: String)

        fun showCopiedToClipBoardToast()
    }

    interface Presenter {

        fun onCreate(savedInstanceState: Bundle?)

        fun onSaveInstanceState(outState: Bundle)

        fun onDestroy()

        fun onNavigationButtonClicked()

        fun itemCount(): Int

        fun itemId(position: Int): Long

        fun onItemBind(viewHolder: ViewHolder, position: Int)

        fun onItemHexClicked(position: Int)

        fun onItemRGBClicked(position: Int)
    }

    interface ViewHolder {

        fun setColor(@ColorInt color: Int)

        fun setName(name: String)

        fun setHex(hex: String)

        fun setRGB(rgb: String)
    }
}