package renetik.android.view.list

import android.view.View

import renetik.android.view.base.CSLayoutId
import renetik.android.view.base.CSView
import renetik.android.view.base.CSViewController

open class CSRowView<RowType : Any> : CSView<View> {

    constructor(parent: CSListController<RowType, *>, layout: CSLayoutId)
            : super(parent, layout)

    constructor(parent: CSViewController<*>) : super(parent)

    lateinit var data: RowType

    fun data(row: RowType) {
        data = row
        onLoad(data)
    }

    protected open fun onLoad(row: RowType) = Unit
}
