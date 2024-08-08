package renetik.android.controller.view.grid

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import renetik.android.controller.base.CSView
import renetik.android.core.extensions.content.displayWidth
import renetik.android.core.kotlin.equalsAny

val <T : CSView<RecyclerView>> T.columnCount: Int
    get() = (view.layoutManager as? GridLayoutManager)?.spanCount ?: 1

fun <T : CSView<RecyclerView>> T.sectionGridLayout(
    columnsCount: Int, nonColumnIds: List<Int>,
) = apply {
    view.layoutManager = GridLayoutManager(this, columnsCount).apply {
        spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int = when {
                isDestructed -> 0
                view.adapter?.getItemViewType(position) equalsAny nonColumnIds -> columnsCount
                else -> 1
            }
        }
    }
}

fun <T : CSView<RecyclerView>> T.sectionGridLayout(
    columnsCount: Int, nonColumnId: Int
) = sectionGridLayout(columnsCount, listOf(nonColumnId))

fun <T : CSView<RecyclerView>> T.autoFitGridLayout(columnWidth: Int) = apply {
    view.layoutManager = GridLayoutManager(this, displayWidth / columnWidth)
}

fun <T : CSView<RecyclerView>> T.columnLayout(columnsCount: Int) = apply {
    view.layoutManager = GridLayoutManager(this, columnsCount)
}