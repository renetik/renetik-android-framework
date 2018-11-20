package renetik.android.extensions.view

import android.widget.GridView

fun GridView.resizeToMatchContentHeight() {
    val gridViewAdapter = this.adapter ?: return
    var totalHeight: Int
    val items = gridViewAdapter.count
    val rows: Int

    val listItem = gridViewAdapter.getView(0, null, this)
    listItem.measure(0, 0)
    totalHeight = listItem.measuredHeight

    var x: Float
    if (items > this.numColumns) {
        x = (items / this.numColumns).toFloat()
        rows = (x + 1).toInt()
        totalHeight *= rows
    }

    val params = this.layoutParams
    params.height = totalHeight
    this.layoutParams = params
}