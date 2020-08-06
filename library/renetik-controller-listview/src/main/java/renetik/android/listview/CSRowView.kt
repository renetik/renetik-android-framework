package renetik.android.listview

import android.view.View
import android.view.ViewGroup
import renetik.android.base.CSLayoutId
import renetik.android.base.CSView
import renetik.android.controller.base.CSViewController

open class CSRowView<RowType : Any> : CSView<View> {

    var onLoad: ((CSRowView<RowType>).(RowType) -> Unit)? = null

    constructor(parent: CSView<out ViewGroup>, layout: CSLayoutId,
                onLoad: ((CSRowView<RowType>).(RowType) -> Unit)? = null)
            : super(parent, layout) {
        this.onLoad = onLoad
    }

    constructor(parent: ViewGroup, layout: CSLayoutId,
                onLoad: ((CSRowView<RowType>).(RowType) -> Unit)? = null)
            : super(parent, layout) {
        this.onLoad = onLoad
    }

    constructor(parent: CSViewController<*>,
                onLoad: ((CSRowView<RowType>).(RowType) -> Unit)? = null)
            : super(parent) {
        this.onLoad = onLoad
    }

    lateinit var data: RowType
    var index = -1

    fun load(data: RowType, index: Int = 0) {
        this.index = index
        this.data = data
        onLoad(data)
    }

    protected open fun onLoad(row: RowType) {
        onLoad?.invoke(this, row)
    }
}

