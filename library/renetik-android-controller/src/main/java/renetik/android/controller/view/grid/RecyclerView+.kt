package renetik.android.controller.view.grid

import android.view.View
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView

//val RecyclerView.viewportWidth get() = computeHorizontalScrollExtent()
val RecyclerView.viewportWidth: Int get() = width
val RecyclerView.viewportHeight: Int get() = height
val RecyclerView.viewportSize: Int
    get() = if (isHorizontal) viewportWidth else viewportHeight

val RecyclerView.itemCount: Int get() = adapter?.itemCount ?: 0
val RecyclerView.itemWidth: Int
    get() = getChildAt(0)?.let { linearLayout.getDecoratedMeasuredWidth(it) } ?: 0

val RecyclerView.itemHeight: Int
    get() = getChildAt(0)?.let { linearLayout.getDecoratedMeasuredHeight(it) } ?: 0

val RecyclerView.displayWidth: Int get() = itemWidth * itemCount
val RecyclerView.displayHeight: Int get() = itemHeight * itemCount
val RecyclerView.displaySize: Int
    get() = if (isHorizontal) displayWidth else displayHeight

val RecyclerView.displayWidthComputed: Int get() = computeHorizontalScrollRange()
val RecyclerView.displayHeightComputed: Int get() = computeVerticalScrollRange()
val RecyclerView.displaySizeComputed: Int
    get() = if (isHorizontal) displayWidthComputed else displayHeightComputed

val RecyclerView.displayWidthScale: Double get() = displayWidth / viewportWidth.toDouble()
val RecyclerView.displayHeightScale: Double get() = displayHeight / viewportHeight.toDouble()
val RecyclerView.displayScale: Double
    get() = if (isHorizontal) displayWidthScale else displayHeightScale

val RecyclerView.scrolledX: Int get() = computeHorizontalScrollOffset()
val RecyclerView.scrolledY: Int get() = computeVerticalScrollOffset()
val RecyclerView.scrolled: Int get() = if (isHorizontal) scrolledX else scrolledY

val RecyclerView.centerScrollX get() = scrolledX + (viewportWidth / 2)
val RecyclerView.centerScrollY get() = scrolledY + (viewportHeight / 2)
val RecyclerView.centerScroll: Int get() = if (isHorizontal) centerScrollX else centerScrollY

fun RecyclerView.isScrolledToEnd(): Boolean {
    val adapterCount = adapter?.itemCount ?: 0
    if (adapterCount == 0) return true
    val lastCompletely = linearLayout.findLastCompletelyVisibleItemPosition()
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
fun RecyclerView.scrollTo(position: Int) =
    if (isHorizontal) scrollToX(position) else scrollToY(position)

fun RecyclerView.scrollToPosition(position: Int, itemSize: Int) {
    val positionStart = position / itemSize
    val offsetInside = -(position % itemSize)
    linearLayout.scrollToPositionWithOffset(positionStart, offsetInside)
}

private val RecyclerView.linearLayout get() = layoutManager as LinearLayoutManager
private val RecyclerView.isHorizontal get() = linearLayout.orientation == HORIZONTAL

fun RecyclerView.visibleChildViews(): List<View> = children.toList()