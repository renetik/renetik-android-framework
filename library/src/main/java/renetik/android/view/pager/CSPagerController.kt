package renetik.android.view.pager

import android.view.View
import androidx.viewpager.widget.ViewPager
import renetik.android.extensions.view.visible
import renetik.android.java.collections.CSList
import renetik.android.java.collections.list
import renetik.android.lang.CSLang.*
import renetik.android.view.adapter.CSOnPageChange
import renetik.android.viewbase.CSViewController

class CSPagerController<PageType>(parent: CSViewController<*>, pagerId: Int)
    : CSViewController<ViewPager>(parent, pagerId)
        where PageType : CSViewController<*>, PageType : CSPagerPage {

    val controllers = list<PageType>()
    var currentIndex = 0
    private var emptyView: View? = null

    constructor(parent: CSViewController<*>, pagerId: Int, pages: CSList<PageType>)
            : this(parent, pagerId) {
        controllers.append(pages)
    }

    fun emptyView(view: View) = apply { emptyView = view.visible(empty(controllers)) }

    fun reload(pages: CSList<PageType>) = apply {
        val currentIndex = view.currentItem
        for (page in controllers) {
            page.onDeinitialize(null)
            page.onDestroy()
        }
        controllers.reload(pages)
        updatePageVisibility(if (pages.length() > currentIndex) currentIndex else 0)
        for (page in pages) page.initialize(state)
        view.setCurrentItem(currentIndex, YES)
        updateView()
    }

    override fun onCreate() {
        super.onCreate()
        CSOnPagerPageChange(this)
                .onDragged { index -> controllers[index].showingInContainer(YES) }
                .onReleased { index -> if (currentIndex != index) controllers[index].showingInContainer(NO) }
        view.addOnPageChangeListener(
                CSOnPageChange { index -> doLater(100) { updatePageVisibility(index) } })
        updateView()
    }

    private fun updateView() {
        view.adapter = CSPagerAdapter(controllers)
        visible(controllers.hasItems)
        emptyView?.visible(controllers.isEmpty())
    }

    private fun updatePageVisibility(newIndex: Int) {
        if (equal(currentIndex, newIndex)) return
        currentIndex = newIndex
        for (index in 0 until controllers.size)
            controllers[index].showingInContainer(if (index == currentIndex) YES else NO)
    }

    fun currentController() = controllers.at(currentIndex)

    fun setCurrentIndex(index: Int) = apply { view.currentItem = index }
}
