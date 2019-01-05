package renetik.android.listview

import android.view.View
import android.widget.AbsListView
import android.widget.AbsListView.OnScrollListener
import android.widget.ListView
import renetik.android.controller.base.CSViewController
import renetik.android.java.extensions.isNull
import renetik.android.view.extensions.hide
import renetik.android.view.extensions.show

class CSListLoadNextController<RowType : Any, ListType : ListView>(
        val parent: CSListController<RowType, ListType>, loadViewLayout: Int
        , val onLoadNext: (CSListLoadNextController<RowType, ListType>) -> Unit)
    : CSViewController<ListType>(parent) {

    private val loadView = inflate<View>(loadViewLayout)
    private var scrollListener: EndlessScrollListener? = null
    private var loading = false
    var pageNumber = 0

    init {
        parent.onLoad.run { _, data -> onListLoad(data) }
    }

    private fun onListLoad(data: List<*>) {
        loadView.hide()
        if (scrollListener.isNull) scrollListener = EndlessScrollListener()
        else if (data.isEmpty()) scrollListener = null
        updateScrollListener()
        loading = false
    }

    private fun updateScrollListener() = view.setOnScrollListener(scrollListener)

    private fun startLoadNext() {
        pageNumber++
        onLoadNext(this)
        loading = true
        loadView.show()
    }

    public override fun onResume() {
        super.onResume()
        updateScrollListener()
        view.addFooterView(loadView)
        view.setFooterDividersEnabled(false)
        loadView.hide()
    }

    private inner class EndlessScrollListener : OnScrollListener {
        private val visibleThreshold = 3
        override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {}
        override fun onScroll(view: AbsListView, first: Int, visible: Int, total: Int) {
            if (total - visible <= 0 || loading) return
            if (total - visible <= first + visibleThreshold) startLoadNext()
        }
    }
}
