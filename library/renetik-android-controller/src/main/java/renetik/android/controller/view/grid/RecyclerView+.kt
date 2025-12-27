package renetik.android.controller.view.grid

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import renetik.android.core.kotlin.unexpected

//val RecyclerView.viewportWidth get() = computeHorizontalScrollExtent()
val RecyclerView.viewportWidth get() = width
val RecyclerView.viewportHeight get() = height
val RecyclerView.itemCount: Int get() = (adapter ?: unexpected()).itemCount
val RecyclerView.itemWidth: Int
    get() {
        val firstChild = getChildAt(0) ?: unexpected()
        val itemWidth = linearLayout.getDecoratedMeasuredWidth(firstChild)
        return itemWidth
    }

val RecyclerView.itemHeight: Int
    get() {
        val firstChild = getChildAt(0) ?: unexpected()
        val itemHeight = linearLayout.getDecoratedMeasuredHeight(firstChild)
        return itemHeight
    }

val RecyclerView.displayWidth: Int get() = itemWidth * itemCount
val RecyclerView.displayHeight: Int get() = itemHeight * itemCount
val RecyclerView.displayWidthComputed get() = computeHorizontalScrollRange()
val RecyclerView.displayHeightComputed get() = computeVerticalScrollRange()
val RecyclerView.displayWidthScale get() = displayWidth / viewportWidth.toDouble()
val RecyclerView.displayHeightScale get() = displayHeight / viewportHeight.toDouble()

val RecyclerView.scrolledX get() = computeHorizontalScrollOffset()
val RecyclerView.scrolledY get() = computeVerticalScrollOffset()
val RecyclerView.centerScrollX get() = scrolledX + (viewportWidth / 2)
val RecyclerView.centerScrollY get() = scrolledY + (viewportHeight / 2)

fun RecyclerView.isScrolledToEnd(): Boolean {
    val manager = layoutManager as? LinearLayoutManager ?: return false
    val adapterCount = adapter?.itemCount ?: 0
    if (adapterCount == 0) return true
    val lastCompletely = manager.findLastCompletelyVisibleItemPosition()
    return lastCompletely == adapterCount - 1
}

fun RecyclerView.scrollToEnd(smooth: Boolean = false) {
    val last = (adapter?.itemCount ?: 1) - 1
    if (last >= 0) {
        if (smooth) smoothScrollToPosition(last)
        else scrollToPosition(last)
    }
}

fun RecyclerView.scrollCenterToX(x: Int) =
    scrollToX(x - (viewportWidth / 2))

fun RecyclerView.scrollCenterToY(y: Int) =
    scrollToY(y - (viewportHeight / 2))

fun RecyclerView.scrollToX(x: Int) = scrollToPosition(x, itemWidth)
fun RecyclerView.scrollToY(y: Int) = scrollToPosition(y, itemHeight)

fun RecyclerView.scrollToPosition(position: Int, itemSize: Int) {
    val positionStart = position / itemSize
    val offsetInside = -(position % itemSize)
    linearLayout.scrollToPositionWithOffset(positionStart, offsetInside)
}

private val RecyclerView.linearLayout get() = layoutManager as LinearLayoutManager

fun RecyclerView.visibleChildViews(): List<View> =
    (0 until childCount).mapNotNull { getChildAt(it) }