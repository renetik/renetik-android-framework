package renetik.android.listview

import android.view.View
import android.view.ViewGroup
import renetik.android.framework.lang.CSLayoutRes
import renetik.android.controller.base.CSView
import renetik.android.controller.base.CSActivityView

open class CSRowView<RowType : Any> : CSView<View> {

    var onLoad: ((CSRowView<RowType>).(RowType) -> Unit)? = null

    constructor(parent: CSView<out ViewGroup>, layout: CSLayoutRes,
                onLoad: ((CSRowView<RowType>).(RowType) -> Unit)? = null)
            : super(parent, layout) {
        this.onLoad = onLoad
    }

    constructor(parent: ViewGroup, layout: CSLayoutRes,
                onLoad: ((CSRowView<RowType>).(RowType) -> Unit)? = null)
            : super(parent, layout) {
        this.onLoad = onLoad
    }

    constructor(parent: CSActivityView<*>,
                onLoad: ((CSRowView<RowType>).(RowType) -> Unit)? = null)
            : super(parent) {
        this.onLoad = onLoad
    }

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

