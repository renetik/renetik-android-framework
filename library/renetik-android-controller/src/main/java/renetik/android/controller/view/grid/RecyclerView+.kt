package renetik.android.controller.view.grid

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import renetik.android.core.kotlin.unexpected

val RecyclerView.scrollOffsetX get() = computeHorizontalScrollOffset()
//val RecyclerView.viewportWidth get() = computeHorizontalScrollExtent()
val RecyclerView.viewportWidth get() = width
val RecyclerView.centerScrollX get() = scrollOffsetX + (viewportWidth / 2)

val RecyclerView.itemWidth: Int
    get() {
        val firstChild = getChildAt(0) ?: unexpected()
        val itemWidth = linearLayout.getDecoratedMeasuredWidth(firstChild)
        return itemWidth
    }

fun RecyclerView.scrollCenterToX(x: Int) =
    scrollToX(x - (viewportWidth / 2))

fun RecyclerView.scrollToX(x: Int) {
    val itemWidth = itemWidth
    val position = x / itemWidth
    val offsetInside = -(x % itemWidth)
    linearLayout.scrollToPositionWithOffset(position, offsetInside)
}

private val RecyclerView.linearLayout
    get() = layoutManager as LinearLayoutManager

val RecyclerView.scrollWidth: Int get() = itemWidth * itemCount
//val RecyclerView.scrollWidth get() = computeHorizontalScrollRange()

val RecyclerView.itemCount: Int
    get() = (adapter ?: unexpected()).itemCount