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
import renetik.android.view.base.CSView
import renetik.android.view.base.CSViewController
import renetik.android.view.base.layout
import renetik.java.event.event
import renetik.java.event.fire
import renetik.java.extensions.isNull
import renetik.java.lang.CSLang

class CSListLoadNextController<ListType : AbsListView>(
        val parent: CSListController<*, ListType>, list: CSListController<*, ListType>, loadViewLayout: Int)
    : CSViewController<ListType>(list) {

    val onLoadNext = event<Unit>()
    private val loadView = CSView<View>(this, layout(loadViewLayout))
    private var scrollListener: EndlessScrollListener? = null
    private var loading = false

    init {
        parent.onLoad.run { _, list -> onListLoad(list) }
    }

    private fun onListLoad(data: List<*>) {
        loadView.hide()
        if (scrollListener.isNull) scrollListener = EndlessScrollListener()
        else if (data.isEmpty()) scrollListener = null
        updateScrollListener()
        loading = false
    }

    private fun updateScrollListener() = view.setOnScrollListener(scrollListener)

    private fun onLoadNext() {
        onLoadNext.fire()
        loading = true
        loadView.show()
    }

    public override fun onResume() {
        super.onResume()
        updateScrollListener()
        (view as? ListView)?.apply {
            addFooterView(loadView.view)
            setFooterDividersEnabled(CSLang.NO)
        } ?: let {
            if (!loadView.hasParent) (parent.view as FrameLayout).addView(loadView.view,
                    LayoutParams(WRAP_CONTENT, WRAP_CONTENT, BOTTOM or CENTER))
        }
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
