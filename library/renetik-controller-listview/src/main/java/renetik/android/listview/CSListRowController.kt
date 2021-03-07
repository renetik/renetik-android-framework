package renetik.android.listview

import android.widget.AbsListView
import renetik.android.R
import renetik.android.base.CSLayoutRes
import renetik.android.base.layout
import renetik.android.controller.base.CSViewController
import renetik.android.listview.CSListRow.RowTypes
import renetik.android.listview.CSListRow.RowTypes.Row

open class CSListRowController<RowType : Any, T : AbsListView>(
    parent: CSViewController<*>, listViewId: Int,
    private val onCreateView: (CSListRowController<RowType, T>).(Int) -> CSRowView<CSListRow<RowType>>?)
    : CSListController<CSListRow<RowType>, T>(parent, listViewId, { viewType ->
    onCreateView(this as CSListRowController<RowType, T>, viewType)
        ?: CSRowView(this, layout(R.layout.cs_empty))
}) {

    fun createRowView(layout: CSLayoutRes,
                      function: ((CSRowView<CSListRow<RowType>>).(RowType) -> Unit)? = null) =
        CSRowView<CSListRow<RowType>>(this, layout) {
            it.data?.let { data -> function?.invoke(this, data) }
        }

    init {
        viewTypesCount = RowTypes.values().size
        onPositionViewType { position -> dataAt(position)!!.type.ordinal }
        onIsEnabled { position -> Row == dataAt(position)!!.type }
    }

}