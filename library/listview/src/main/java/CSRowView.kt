package renetik.android.listview

import android.view.View
import renetik.android.base.CSLayoutId
import renetik.android.base.CSView
import renetik.android.controller.base.CSViewController

open class CSRowView<RowType : Any> : CSView<View> {

    var onLoad: ((CSRowView<RowType>).(RowType) -> Unit)? = null

    constructor(parent: CSListController<RowType, *>, layout: CSLayoutId,
                onLoad: ((CSRowView<RowType>).(RowType) -> Unit)? = null)
            : super(parent, layout) {
        this.onLoad = onLoad
    }

    constructor(parent: CSViewController<*>, onLoad: ((CSRowView<RowType>).(RowType) -> Unit)? = null)
            : super(parent) {
        this.onLoad = onLoad
    }

    lateinit var data: RowType
    var index = -1

    fun load(index: Int, data: RowType) {
        this.index = index
        this.data = data
        onLoad(data)
    }

    protected open fun onLoad(row: RowType) {
        onLoad?.invoke(this, row)
    }
}
