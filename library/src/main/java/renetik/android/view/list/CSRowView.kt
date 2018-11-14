package renetik.android.view.list

import android.view.View

import renetik.android.viewbase.CSLayoutId
import renetik.android.viewbase.CSView

open class CSRowView<RowType : Any>(parent: CSListController<RowType, *>, layout: CSLayoutId) : CSView<View>(parent, layout) {

    lateinit var data: RowType

    fun data(row: RowType) {
        data = row
        onLoad(data)
    }

    protected open fun onLoad(row: RowType) = Unit
}
