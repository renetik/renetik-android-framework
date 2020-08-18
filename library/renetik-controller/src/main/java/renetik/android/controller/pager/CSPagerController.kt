package renetik.android.controller.pager

import android.content.Context
import android.view.View
import android.widget.Scroller
import androidx.viewpager.widget.ViewPager
import renetik.android.controller.base.CSViewController
import renetik.android.java.event.event
import renetik.android.java.event.listener
import renetik.android.java.extensions.collections.*
import renetik.android.java.extensions.isEmpty
import renetik.android.java.extensions.later
import renetik.android.java.extensions.setPrivateField
import renetik.android.view.extensions.shownIf

class CSPagerController<PageType>(parent: CSViewController<*>, pagerId: Int) :
    CSViewController<ViewPager>(parent, pagerId)
        where PageType : CSViewController<*>, PageType : CSPagerPage {

    val eventOnPageChange = event<PageType>()
    fun onPageChange(function: (PageType) -> Unit) = eventOnPageChange.listener(function)

    val pageCount: Int get() = controllers.size

    val controllers = list<PageType>()
    var currentIndex: Int? = null
    private var emptyView: View? = null

    constructor(parent: CSViewController<*>, pagerId: Int, pages: List<PageType>)
            : this(parent, pagerId) {
        controllers.putAll(pages)
    }

    init {
        view.setPrivateField("mScroller", CSPagerScroller(this))
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
                if (index == view.currentItem)
                    updatePageVisibility(index)
            }
            .onSelected { index -> currentIndex = index }
        updatePageVisibility(0)
        view.setCurrentItem(0, true)
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
        eventOnPageChange.fire(current)
        hideKeyboard()
    }

    val current get() = controllers.at(currentIndex!!)!!

    // Bug in pager , animation work just when delayed
    fun setActive(index: Int, animated: Boolean = true) = apply {
        if (animated) later { view.setCurrentItem(index, true) }
        else view.setCurrentItem(index, false)
    }

    fun showPage(page: PageType) {
        if (!controllers.contains(page)) add(page)
        setActive(index = controllers.indexOf(page))

    }

    fun isPrevious(page: PageType): Boolean {
        val indexOfPage = controllers.indexOf(page)
        return indexOfPage != -1 && indexOfPage == currentIndex!! - 1
    }

    val isOnLastPage: Boolean get() = currentIndex == pageCount - 1
}

class CSPagerScroller(context: Context) : Scroller(context) {

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) =
        super.startScroll(startX, startY, dx, dy, 700)

}