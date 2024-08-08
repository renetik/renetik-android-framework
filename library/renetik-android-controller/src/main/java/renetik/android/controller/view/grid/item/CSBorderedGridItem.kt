package renetik.android.controller.view.grid.item

import android.view.View
import renetik.android.controller.view.grid.GridViewOut
import renetik.android.controller.view.grid.columnCount
import renetik.android.controller.view.grid.dataCount
import renetik.android.ui.extensions.view.gone
import renetik.android.ui.extensions.view.visible

interface CSBorderedGridItem {
    val topBorder: View? get() = null
    val rightBorder: View
    val bottomBorder: View

    fun updateBorders(grid: GridViewOut<*>, index: Int) =
        updateBorders(grid.dataCount, grid.columnCount, index)

    fun updateBorders(grid: GridViewOut<*>,
        item: CSSectionItem<out CSItemSection<*>, *>) =
        updateBorders(item.section.items.size, grid.columnCount, item.index)

    fun updateBorders(count: Int, columns: Int, index: Int) {
        topBorder?.gone()

        // Calculate if item is in the last column
        val isLastInColumn = (index + 1) % columns == 0

        // Calculate if item is in the last row
        val isLastInRow = index >= count - columns

        // Calculate if there are empty spaces below the item
        val hasEmptySpaceBelow = index >= count - columns && count % columns != 0

        // Check if item is the last in a grid where items fit perfectly into columns
        val isPerfectGridLastItem = count % columns == 0 && (index + 1) == count

        // Right border visibility
        rightBorder.visible(
            !(isLastInColumn || isPerfectGridLastItem)
        )

        // Bottom border visibility
        bottomBorder.visible(
            !isLastInRow || hasEmptySpaceBelow
        )
    }
}