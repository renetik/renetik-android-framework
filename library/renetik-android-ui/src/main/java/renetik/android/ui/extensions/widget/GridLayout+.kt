package renetik.android.ui.extensions.widget

import android.view.View
import android.widget.GridLayout
import android.widget.GridLayout.LayoutParams
import android.widget.GridLayout.UNDEFINED
import android.widget.GridLayout.spec

fun GridLayout.fillColumns(count: Int) {
    val children = mutableListOf<View>()
    repeat(childCount) { children.add(getChildAt(it)) }
    removeAllViews()
    columnCount = count
    children.forEach {
        it.layoutParams = LayoutParams().apply {
            width = 0; height = 0
            rowSpec = spec(UNDEFINED, 1f)
            columnSpec = spec(UNDEFINED, 1f)
        }
        addView(it)
    }
}