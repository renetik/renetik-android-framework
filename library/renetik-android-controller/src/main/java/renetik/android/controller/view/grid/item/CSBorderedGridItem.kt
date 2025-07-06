package renetik.android.controller.view.grid.item

import android.view.View
import renetik.android.controller.view.grid.GridViewOut
import renetik.android.controller.view.grid.columnCount
import renetik.android.controller.view.grid.dataCount
import renetik.android.ui.extensions.view.visible

interface CSBorderedGridItem {
    val rightBorder: View
    val bottomBorder: View

    fun updateBorders(grid: GridViewOut<*>, index: Int,
        isBottomBorder: Boolean = false, isLastRightBorder: Boolean = false) =
        updateBorders(grid.dataCount, grid.columnCount, index,
            isBottomBorder, isLastRightBorder)

    fun updateBorders(grid: GridViewOut<*>,
        item: CSSectionItem<out CSItemSection<*>, *>,
        isBottomBorder: Boolean = false,
        isLastRightBorder: Boolean = false) =
        updateBorders(item.section.items.size, grid.columnCount,
            item.index, isBottomBorder, isLastRightBorder)

    fun updateBorders(count: Int, columns: Int, index: Int,
        isBottomBorder: Boolean = false,
        isLastRightBorder: Boolean = false) {

        val isLast = index == count - 1
        // Calculate if the item is in the last column
        val isLastInColumn = (index + 1) % columns == 0

        // Calculate the number of full rows
        val fullRows = count / columns

        val isPerfectGrid = (count % columns == 0)

        // Determine if the item is in the last row
        val isInLastRow = if (isPerfectGrid) index >= count - columns
        else index >= fullRows * columns

        // Determine if the grid has a perfect number of items filling all columns
        val isPerfectGridLastItem = (index + 1 == count) && isPerfectGrid

        // Set right border visibility
        rightBorder.visible(!(isLastInColumn ||
                isPerfectGridLastItem || (isLast && !isLastRightBorder)))

        // Set bottom border visibility based on conditions
        val hasNextItemBelow = index + columns < count
//        bottomBorder.visible(isBottomBorder || hasNextItemBelow)
        bottomBorder.visible((isBottomBorder && !isInLastRow) || hasNextItemBelow)
//        bottomBorder.visible(isBottomBorder || (!isInLastRow && hasNextItemBelow))
    }
}