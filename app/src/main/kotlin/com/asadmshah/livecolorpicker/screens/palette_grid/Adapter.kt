package com.asadmshah.livecolorpicker.screens.palette_grid

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.asadmshah.livecolorpicker.R

internal class Adapter(private val presenter: PaletteGridContract.Presenter): RecyclerView.Adapter<ViewHolder>() {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return presenter.itemId(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.viewholder_palette_grid_item, parent, false)
        val viewHolder = ViewHolder(view)

        viewHolder.viewHex.setOnClickListener { presenter.onItemHexClicked(viewHolder.adapterPosition) }
        viewHolder.viewRGB.setOnClickListener { presenter.onItemRGBClicked(viewHolder.adapterPosition) }

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        presenter.onItemBind(holder, position)
    }

    override fun getItemCount(): Int {
        return presenter.itemCount()
    }
}
