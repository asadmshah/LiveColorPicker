package com.asadmshah.livecolorpicker.screens.palette_grid

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.widget.Toast
import com.asadmshah.livecolorpicker.R
import com.asadmshah.livecolorpicker.models.ColorPalette
import com.asadmshah.livecolorpicker.widgets.BaseActivity

class PaletteGridActivity : BaseActivity(), PaletteGridContract.View {

    companion object {
        private const val KEY_COLOR_PALETTE = "color_palette"

        fun createIntent(context: Context, colorPalette: ColorPalette): Intent {
            val intent = Intent(context, PaletteGridActivity::class.java)
            intent.putExtra(KEY_COLOR_PALETTE, colorPalette)
            return intent
        }
    }

    private val viewToolbar by lazyView<Toolbar>(R.id.toolbar)
    private val viewList by lazyView<RecyclerView>(R.id.list_view) {
        it.layoutManager = GridLayoutManager(PaletteGridActivity@ this, 2)
    }

    private var adapter: Adapter? = null

    private val presenter by lazy {
        val colorPalette = intent.getParcelableExtra<ColorPalette>(KEY_COLOR_PALETTE)
        PaletteGridPresenter(colorPalette, PaletteGridActivity@ this, component)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_palette_grid)

        viewToolbar.setNavigationOnClickListener { presenter.onNavigationButtonClicked() }

        presenter.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        presenter.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()

        presenter.onDestroy()
    }

    override fun navigateUp() {
        super.onBackPressed()
    }

    override fun enableList() {
        adapter = Adapter(presenter)
        viewList.adapter = adapter
    }

    override fun notifyDataSetChanged() {
        adapter?.notifyDataSetChanged()
    }

    override fun setProgressBarEnabled(enabled: Boolean) {

    }

    override fun clipboardPut(key: String, value: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val data = ClipData.newPlainText(key, value)
        clipboard.primaryClip = data
    }

    override fun showCopiedToClipBoardToast() {
        Toast.makeText(this, R.string.text_copied, Toast.LENGTH_SHORT).show()
    }
}