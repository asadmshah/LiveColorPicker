package com.asadmshah.livecolorpicker.screens.palette_grid

import android.os.Bundle
import com.asadmshah.livecolorpicker.models.ColorPalette
import com.asadmshah.livecolorpicker.screens.ActivityComponent

internal class PaletteGridPresenter(private val colorPalette: ColorPalette, private val view: PaletteGridContract.View, component: ActivityComponent) : PaletteGridContract.Presenter {

    override fun onCreate(savedInstanceState: Bundle?) {
        view.enableList()
    }

    override fun onSaveInstanceState(outState: Bundle) {

    }

    override fun onDestroy() {

    }

    override fun onNavigationButtonClicked() {
        view.navigateUp()
    }

    override fun itemCount(): Int {
        return colorPalette.colors.size
    }

    override fun itemId(position: Int): Long {
        return position.toLong()
    }

    override fun onItemBind(viewHolder: PaletteGridContract.ViewHolder, position: Int) {
        colorPalette.colors[position].let {
            viewHolder.setColor(it.rgb)
            viewHolder.setName(it.name)
            viewHolder.setHex(String.format("#%06X", (0xFFFFFF and it.rgb)))
            viewHolder.setRGB("${(it.rgb shr 16) and 0xFF}, ${(it.rgb shr 8) and 0xFF}, ${it.rgb and 0xFF}")
        }
    }

    override fun onItemHexClicked(position: Int) {
        colorPalette.colors[position].let {
            val hex = String.format("#%06X", (0xFFFFFF and it.rgb))
            view.clipboardPut("Hex", hex)
        }
        view.showCopiedToClipBoardToast()
    }

    override fun onItemRGBClicked(position: Int) {
        colorPalette.colors[position].let {
            val rgb = "${(it.rgb shr 16) and 0xFF}, ${(it.rgb shr 8) and 0xFF}, ${it.rgb and 0xFF}"
            view.clipboardPut("RGB", rgb)
        }
        view.showCopiedToClipBoardToast()
    }

}