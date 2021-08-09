package renetik.android.listview

import android.view.View
import android.view.ViewGroup
import renetik.android.framework.CSView
import renetik.android.framework.lang.CSLayoutRes

open class CSRowView<RowType : Any>(
    parent: CSView<out ViewGroup>,
    layout: CSLayoutRes,
    var onLoad: ((CSRowView<RowType>).(RowType) -> Unit)? = null)
    : CSView<View>(parent, layout) {

    lateinit var row: RowType
    var index = -1

    fun load(row: RowType, index: Int = 0) {
        this.index = index
        this.row = row
        onLoad(row)
    }

    open fun onLoad(row: RowType) {
        onLoad?.invoke(this, row)
    }
}

