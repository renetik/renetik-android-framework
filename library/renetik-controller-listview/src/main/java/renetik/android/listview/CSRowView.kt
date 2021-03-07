package renetik.android.listview

import android.view.View
import android.view.ViewGroup
import renetik.android.base.CSLayoutRes
import renetik.android.controller.base.CSView
import renetik.android.controller.base.CSViewController

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

    constructor(parent: CSViewController<*>,
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

    protected open fun onLoad(row: RowType) {
        onLoad?.invoke(this, row)
    }
}

