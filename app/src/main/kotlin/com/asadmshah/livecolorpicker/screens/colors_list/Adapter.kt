package com.asadmshah.livecolorpicker.screens.colors_list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.asadmshah.livecolorpicker.R

internal class Adapter(private val presenter: ColorsListContract.Presenter): RecyclerView.Adapter<ViewHolder>() {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return presenter.getItemId(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.viewholder_colors_list_item, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener { presenter.onListItemClicked(viewHolder.adapterPosition) }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return presenter.getItemCount()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        presenter.onBindViewHolder(holder, position)
    }
}