package com.asadmshah.livecolorpicker.screens.colors_list

import android.os.Bundle
import com.asadmshah.livecolorpicker.database.ColorsStore
import com.asadmshah.livecolorpicker.models.ColorPalette
import com.asadmshah.livecolorpicker.screens.ActivityComponent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ColorsListPresenter(private val view: ColorsListContract.View, component: ActivityComponent): ColorsListContract.Presenter {

    @Inject lateinit var colorsStore: ColorsStore

    var colorPaletteList: MutableList<ColorPalette>? = null
    var colorPaletteListDisposable: Disposable? = null

    init {
        component.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        view.enableList()

        requestContent()
    }

    override fun onSaveInstanceState(outState: Bundle) {

    }

    override fun onDestroy() {
        colorPaletteListDisposable?.dispose()
    }

    override fun getItemCount(): Int {
        return colorPaletteList?.size ?: 0
    }

    override fun getItemId(position: Int): Long {
        return colorPaletteList?.get(position)?.date?.time ?: -1
    }

    override fun onBindViewHolder(viewHolder: ColorsListContract.ViewHolder, position: Int) {
        colorPaletteList?.get(position)?.let {
            viewHolder.setDate(it.date)
            viewHolder.setColorList(it.colors)
        }
    }

    override fun onListItemClicked(position: Int) {

    }

    override fun onNavigationButtonClicked() {
        view.navigateUp()
    }

    override fun onPullToRefresh() {
        requestContent()
    }

    fun requestContent() {
        colorPaletteListDisposable?.dispose()

        view.setProgressBarEnabled(true)
        colorPaletteListDisposable = colorsStore
                .read()
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { items, _ ->
                    colorPaletteList = items
                    view.notifyDataSetChanged()
                    view.setProgressBarEnabled(false)
                }
    }
}