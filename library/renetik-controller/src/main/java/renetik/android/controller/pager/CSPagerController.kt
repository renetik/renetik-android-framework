package renetik.android.controller.pager

import android.view.View
import androidx.viewpager.widget.ViewPager
import renetik.android.controller.base.CSViewController
import renetik.android.java.extensions.collections.*
import renetik.android.java.extensions.isEmpty
import renetik.android.task.later
import renetik.android.view.adapter.CSOnPageSelected
import renetik.android.view.extensions.shown

class CSPagerController<PageType>(parent: CSViewController<*>, pagerId: Int) :
    CSViewController<ViewPager>(parent, pagerId)
        where PageType : CSViewController<*>, PageType : CSPagerPage {

    val pageCount: Int get() = controllers.size

    val controllers = list<PageType>()
    var currentIndex: Int? = null
    private var emptyView: View? = null

    constructor(parent: CSViewController<*>, pagerId: Int, pages: List<PageType>)
            : this(parent, pagerId) {
        controllers.putAll(pages)
    }

    fun emptyView(view: View) = apply { emptyView = view.shown(controllers.isEmpty) }

    fun reload(pages: List<PageType>) = apply {
        val currentIndex = view.currentItem
        for (page in controllers) page.deInitialize()
        controllers.reload(pages)
        updatePageVisibility(if (pages.size > currentIndex) currentIndex else 0)
        for (page in pages) page.initialize()
        view.setCurrentItem(currentIndex, true)
        updateView()
    }

    fun add(page: PageType) = apply {
        controllers.add(page)
        page.initialize()
        updateView()
    }

    override fun onCreate() {
        super.onCreate()
        CSOnPagerPageChange(this)
            .onDragged { index -> controllers[index].showingInContainer(true) }
            .onReleased { index ->
                if (currentIndex != index) controllers[index].showingInContainer(
                    false
                )
            }
        view.addOnPageChangeListener(
            CSOnPageSelected { index -> later(100) { updatePageVisibility(index) } })
        updatePageVisibility(0)
        view.setCurrentItem(0, true)
        updateView()
    }

    private fun updateView() {
        view.adapter = CSPagerAdapter(controllers)
        view.shown(controllers.hasItems)
        emptyView?.shown(controllers.isEmpty())
    }

    private fun updatePageVisibility(newIndex: Int) {
        if (currentIndex == newIndex) return
        currentIndex = newIndex
        for (index in 0 until controllers.size)
            controllers[index].showingInContainer(index == currentIndex)
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