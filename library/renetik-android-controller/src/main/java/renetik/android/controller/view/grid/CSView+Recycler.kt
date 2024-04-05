package renetik.android.controller.view.grid

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import renetik.android.controller.base.CSView
import renetik.android.core.extensions.content.displayWidth

val <T : CSView<RecyclerView>> T.columnCount: Int
    get() = (view.layoutManager as? GridLayoutManager)?.spanCount ?: 1

fun <T : CSView<RecyclerView>> T.sectionGridLayout(
    columnsCount: Int, headerId: Int,
) = apply {
    val layoutManager = GridLayoutManager(this, columnsCount)
    layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            if (isDestructed) return 0
            return if (view.adapter?.getItemViewType(position) == headerId)
                columnsCount else 1
        }
    }
    view.layoutManager = layoutManager
}

fun <T : CSView<RecyclerView>> T.autoFitGridLayout(columnWidth: Int) = apply {
    view.layoutManager = GridLayoutManager(this, displayWidth / columnWidth)
}

fun <T : CSView<RecyclerView>> T.columnLayout(columnsCount: Int) = apply {
    view.layoutManager = GridLayoutManager(this, columnsCount)
}