package renetik.android.controller.view.grid

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import renetik.android.core.kotlin.unexpected

//val RecyclerView.viewportWidth get() = computeHorizontalScrollExtent()
val RecyclerView.viewportWidth get() = width
val RecyclerView.itemCount: Int get() = (adapter ?: unexpected()).itemCount
val RecyclerView.itemWidth: Int
    get() {
        val firstChild = getChildAt(0) ?: unexpected()
        val itemWidth = linearLayout.getDecoratedMeasuredWidth(firstChild)
        return itemWidth
    }

val RecyclerView.displayWidth: Int get() = itemWidth * itemCount
val RecyclerView.displayWidthComputed get() = computeHorizontalScrollRange()
val RecyclerView.displayScale get() = displayWidth / viewportWidth.toDouble()

val RecyclerView.scrolledX get() = computeHorizontalScrollOffset()
val RecyclerView.centerScrollX get() = scrolledX + (viewportWidth / 2)

fun RecyclerView.isScrolledToEnd(): Boolean {
    val manager = layoutManager as? LinearLayoutManager ?: return false
    val adapterCount = adapter?.itemCount ?: 0
    if (adapterCount == 0) return true
    val lastCompletely = manager.findLastCompletelyVisibleItemPosition()
    return lastCompletely == adapterCount - 1
}

fun RecyclerView.scrollToEnd() {
    val last = (adapter?.itemCount ?: 1) - 1
    if (last >= 0) {
        scrollToPosition(last)
//        smoothScrollToPosition(last)
    }
}

fun RecyclerView.scrollCenterToX(x: Int) =
    scrollToX(x - (viewportWidth / 2))

fun RecyclerView.scrollToX(x: Int) = scrollToX(x, itemWidth)

fun RecyclerView.scrollToX(x: Int, itemWidth: Int) {
    val position = x / itemWidth
    val offsetInside = -(x % itemWidth)
    linearLayout.scrollToPositionWithOffset(position, offsetInside)
}

private val RecyclerView.linearLayout get() = layoutManager as LinearLayoutManager