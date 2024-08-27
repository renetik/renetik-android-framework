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

    fun updateBorders(grid: GridViewOut<*>, index: Int,
        isBottomBorder: Boolean = false) =
        updateBorders(grid.dataCount, grid.columnCount, index, isBottomBorder)

    fun updateBorders(grid: GridViewOut<*>,
        item: CSSectionItem<out CSItemSection<*>, *>,
        isBottomBorder: Boolean = false) =
        updateBorders(item.section.items.size, grid.columnCount,
            item.index, isBottomBorder)

    fun updateBorders(count: Int, columns: Int, index: Int,
        isBottomBorder: Boolean = false) {
        // Hide the top border by default
        topBorder?.gone()

        // Calculate if the item is in the last column
        val isLastInColumn = (index + 1) % columns == 0

        // Calculate the number of full rows
        val fullRows = count / columns

        val isPerfectGrid = (count % columns == 0)

        // Determine if the item is in the last row
        val isInLastRow = if (isPerfectGrid) index >= count - columns
        else index >= fullRows * columns

        // Determine if there are empty spaces below the item in the last row
//        val hasEmptySpaceBelow = isInLastRow && count % columns != 0 && index < count - 1

        // Determine if the grid has a perfect number of items filling all columns
        val isPerfectGridLastItem = (index + 1 == count) && isPerfectGrid

        // Set right border visibility
        rightBorder.visible(!(isLastInColumn || isPerfectGridLastItem))

        if (!isBottomBorder) bottomBorder.visible(!isInLastRow)
        else bottomBorder.visible(true)

        bottomBorder.visible(isBottomBorder || !isInLastRow)
    }
}