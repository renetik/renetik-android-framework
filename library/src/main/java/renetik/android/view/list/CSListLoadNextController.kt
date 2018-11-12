package renetik.android.view.list

import android.view.Gravity.BOTTOM
import android.view.Gravity.CENTER
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.AbsListView
import android.widget.AbsListView.OnScrollListener
import android.widget.FrameLayout
import android.widget.FrameLayout.LayoutParams
import android.widget.ListView
import renetik.android.lang.CSLang.*
import renetik.android.viewbase.CSView
import renetik.android.viewbase.CSViewController

class CSListLoadNextController(parent: CSListController<*, *>, loadViewLayout: Int)
    : CSViewController<View>(parent) {

    val onLoadNext = event<Void>()
    private val loadView = CSView<View>(this, CSView.layout(loadViewLayout))
    private var scrollListener: EndlessScrollListener? = null
    private var loading = false

    init {
        parent.onLoad.run { _, list -> onListLoad(list) }
    }

    private fun onListLoad(data: List<*>) {
        loadView.hide()
        if (no(scrollListener)) scrollListener = EndlessScrollListener()
        else if (data.isEmpty()) scrollListener = null
        updateScrollListener()
        loading = false
    }

    private fun updateScrollListener() = asAbsListView().setOnScrollListener(scrollListener)

    private fun onLoadNext() {
        fire(onLoadNext)
        loading = true
        loadView.show()
    }

    public override fun onResume() {
        super.onResume()
        updateScrollListener()
        if (view is ListView) {
            asListView().addFooterView(loadView.view)
            asListView().setFooterDividersEnabled(NO)
        } else if (!loadView.hasParent())
            (view!!.parent.parent as FrameLayout).addView(loadView.view,
                    LayoutParams(WRAP_CONTENT, WRAP_CONTENT, BOTTOM or CENTER))
        loadView.hide()
    }

    private inner class EndlessScrollListener : OnScrollListener {
        private val visibleThreshold = 3

        override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {}

        override fun onScroll(view: AbsListView, first: Int, visible: Int, total: Int) {
            if (total - visible <= 0 || loading) return
            if (total - visible <= first + visibleThreshold) onLoadNext()
        }
    }
}
