package com.asadmshah.livecolorpicker.screens.colors_list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import com.asadmshah.livecolorpicker.R
import com.asadmshah.livecolorpicker.widgets.BaseActivity

class ColorsListActivity: BaseActivity(), ColorsListContract.View {

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, ColorsListActivity::class.java)
        }
    }

    private val viewToolbar by lazyView<Toolbar>(R.id.toolbar)
    private val viewSwipeRefresh by lazyView<SwipeRefreshLayout>(R.id.swipe_refresh)
    private val viewList by lazyView<RecyclerView>(R.id.list_view)

    private val presenter by lazy { ColorsListPresenter(ColorsListActivity@this, component) }

    private var adapter: Adapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_colors_list)

        viewToolbar.setNavigationOnClickListener { presenter.onNavigationButtonClicked() }

        viewSwipeRefresh.setOnRefreshListener { presenter.onPullToRefresh() }

        viewList.layoutManager = LinearLayoutManager(this)
        viewList.setHasFixedSize(true)

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
        super.onBackPressed() // Cheatcodes
    }

    override fun enableList() {
        adapter = Adapter(presenter)
        viewList.adapter = adapter
    }

    override fun notifyDataSetChanged() {
        adapter?.notifyDataSetChanged()
    }

    override fun setProgressBarEnabled(enabled: Boolean) {
        viewSwipeRefresh.isRefreshing = enabled
    }
}