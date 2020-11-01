package renetik.android.controller.pager

import android.view.View
import renetik.android.controller.base.CSViewController
import renetik.android.java.event.event
import renetik.android.java.event.listen
import renetik.android.java.extensions.collections.*
import renetik.android.java.extensions.isEmpty
import renetik.android.java.extensions.later
import renetik.android.view.extensions.shownIf

class CSPagerController<PageType>(parent: CSViewController<*>, pagerId: Int) :
    CSViewController<CSViewPager>(parent, pagerId)
        where PageType : CSViewController<*>, PageType : CSPagerPage {

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

    constructor(parent: CSViewController<*>, pagerId: Int, pages: List<PageType>)
            : this(parent, pagerId) {
        controllers.putAll(pages)
    }

    fun emptyView(view: View) = apply { emptyView = view.shownIf(controllers.isEmpty) }

    fun reload(pages: List<PageType>) = apply {
        val currentIndex = view.currentItem
        for (page in controllers) page.lifecycleDeInitialize()
        controllers.reload(pages)
        updatePageVisibility(if (pages.size > currentIndex) currentIndex else 0)
        for (page in pages) page.lifecycleInitialize()
        view.setCurrentItem(currentIndex, true)
        updateView()
    }

    fun add(page: PageType) = apply {
        controllers.add(page)
        page.lifecycleInitialize()
        updateView()
    }

    override fun onCreate() {
        super.onCreate()
        CSOnPagerPageChange(this)
            .onDragged { index -> controllers[index].showingInContainer(true) }
            .onReleased { index ->
                if (currentIndex == view.currentItem)
                    updatePageVisibility(view.currentItem)
                else if (index == view.currentItem)
                    updatePageVisibility(index)
            }
            .onSelected { index ->
                currentIndex = index
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
        view.shownIf(controllers.hasItems)
        emptyView?.shownIf(controllers.isEmpty())
    }

    private fun updatePageVisibility(newIndex: Int) {
        currentIndex = newIndex
        for (index in 0 until controllers.size)
            controllers[index].showingInContainer(index == currentIndex)
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
            updatePageVisibility(0)
        }
        setActive(index = controllers.indexOf(page), animated)
    }

    fun isPrevious(page: PageType): Boolean {
        val indexOfPage = controllers.indexOf(page)
        return indexOfPage != -1 && indexOfPage == currentIndex!! - 1
    }

    val isOnLastPage: Boolean get() = currentIndex == pageCount - 1
}