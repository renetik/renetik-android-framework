package renetik.android.controller.pager

import android.view.View
import renetik.android.controller.base.CSActivityView
import renetik.android.core.kotlin.collections.at
import renetik.android.core.kotlin.collections.hasItems
import renetik.android.core.kotlin.collections.list
import renetik.android.core.kotlin.collections.putAll
import renetik.android.core.kotlin.collections.reload
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.util.CSLater.later
import renetik.android.ui.extensions.view.show

class CSPagerView<PageType>(parent: CSActivityView<*>, pagerId: Int) :
    CSActivityView<CSViewPager>(parent, pagerId)
    where PageType : CSActivityView<*>, PageType : CSPagerPage {

    val eventOnPageChange = event<PageType>()
    fun onPageChange(function: (PageType) -> Unit) = eventOnPageChange.listen(function)
    val pageCount: Int get() = controllers.size
    val controllers = list<PageType>()
    var currentIndex: Int? = null
    var isSwipePagingEnabled
        get() = view.isSwipePagingEnabled
        set(value) {
            view.isSwipePagingEnabled = value
        }
    private var emptyView: View? = null

    constructor(parent: CSActivityView<*>, pagerId: Int, pages: List<PageType>)
        : this(parent, pagerId) {
        controllers.putAll(pages)
    }

    fun emptyView(view: View) = apply { emptyView = view.show(controllers.isEmpty()) }

    fun reload(pages: List<PageType>) = apply {
        val currentIndex = view.currentItem
        for (page in controllers) page.lifecycleStop()
        controllers.reload(pages)
        updatePageVisibility(if (pages.size > currentIndex) currentIndex else 0)
        for (page in pages) page.lifecycleUpdate()
        view.setCurrentItem(currentIndex, true)
        updateView()
    }

    fun add(page: PageType) = apply {
        controllers.add(page)
        page.lifecycleUpdate()
        updateView()
    }

    override fun onViewShowingFirstTime() {
        super.onViewShowingFirstTime()
        CSOnPagerPageChange(this)
            .onDragged { index -> controllers[index].showingInPager(true) }
            .onReleased { index ->
                if (currentIndex == view.currentItem)
                    updatePageVisibility(view.currentItem)
                else if (index == view.currentItem)
                    updatePageVisibility(index)
            }
            .onSelected { index ->
                updatePageVisibility(index)
            }
        if (controllers.hasItems) {
            updatePageVisibility(0)
            view.setCurrentItem(0, true)
        }
        updateView()
    }

    private fun updateView() {
        view.adapter?.notifyDataSetChanged()
            ?: let { view.adapter = CSPagerAdapter(controllers) }
        view.show(controllers.hasItems)
        emptyView?.show(controllers.isEmpty())
    }

    private fun updatePageVisibility(newIndex: Int) {
        if (currentIndex == newIndex) return
        currentIndex = newIndex
        for (index in 0 until controllers.size)
            controllers[index].showingInPager(index == currentIndex)
        eventOnPageChange.fire(current!!)
        hideKeyboard()
    }

    val current get() = currentIndex?.let { controllers.at(it) }

    // Bug in pager , animation work just when delayed
    private fun setActive(index: Int, animated: Boolean = true) = apply {
        if (animated) later { view.setCurrentItem(index, true) }
        else view.setCurrentItem(index, false)
    }

    fun showPage(page: PageType, animated: Boolean = true) {
        if (!controllers.contains(page)) {
            add(page)
            // OnPageChangeListener onSelected not called for first index
            if (controllers.size == 1) updatePageVisibility(0)
        }
        setActive(index = controllers.indexOf(page), animated)
    }

    fun isPrevious(page: PageType): Boolean {
        val indexOfPage = controllers.indexOf(page)
        return indexOfPage != -1 && indexOfPage == currentIndex!! - 1
    }

    val isOnLastPage: Boolean get() = currentIndex == pageCount - 1
}