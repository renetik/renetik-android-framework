package renetik.android.controller.view.grid

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import renetik.android.core.kotlin.unexpected

val RecyclerView.scrollOffsetX get() = computeHorizontalScrollOffset()
val RecyclerView.viewportWidth get() = computeHorizontalScrollExtent()
val RecyclerView.scrollOffsetCenterX get() = scrollOffsetX + (viewportWidth / 2)

val RecyclerView.itemWidth: Int
    get() {
        val firstChild = getChildAt(0) ?: unexpected()
        val itemWidth = linearLayout.getDecoratedMeasuredWidth(firstChild)
        return itemWidth
    }

fun RecyclerView.scrollToOffset(offsetPx: Int) {
    val itemWidth = itemWidth
    val position = offsetPx / itemWidth
    val offsetInside = -(offsetPx % itemWidth)
    linearLayout.scrollToPositionWithOffset(position, offsetInside)
}

private val RecyclerView.linearLayout
    get() = layoutManager as LinearLayoutManager

val RecyclerView.scrollWidth: Int get() = itemWidth * itemCount
//val RecyclerView.scrollWidth get() = computeHorizontalScrollRange()

val RecyclerView.itemCount: Int
    get() = (adapter ?: unexpected()).itemCount