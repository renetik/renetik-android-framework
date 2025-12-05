package renetik.android.controller.view.grid

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

val RecyclerView.scrollOffsetX get() = computeHorizontalScrollOffset()
val RecyclerView.viewport get() = computeHorizontalScrollExtent()
val RecyclerView.scrollOffsetCenterX get() = scrollOffsetX + (viewport / 2)

fun RecyclerView.scrollToOffset(offsetPx: Int) {
    val layout = layoutManager as LinearLayoutManager
    val itemWidth = getChildAt(0)?.width ?: return
    val position = offsetPx / itemWidth
    val offsetInside = -(offsetPx % itemWidth)
    layout.scrollToPositionWithOffset(position, offsetInside)
}
