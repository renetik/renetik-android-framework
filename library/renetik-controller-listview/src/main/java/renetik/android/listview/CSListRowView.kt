package renetik.android.listview

import android.widget.AbsListView
import renetik.android.R
import renetik.android.framework.lang.CSLayoutRes
import renetik.android.framework.lang.CSLayoutRes.Companion.layout
import renetik.android.controller.base.CSActivityView
import renetik.android.listview.CSListRow.RowTypes
import renetik.android.listview.CSListRow.RowTypes.Row

open class CSListRowView<RowType : Any, T : AbsListView>(
    parent: CSActivityView<*>, listViewId: Int,
    private val onCreateView: (CSListRowView<RowType, T>).(Int) -> CSRowView<CSListRow<RowType>>?)
    : CSListView<CSListRow<RowType>, T>(parent, listViewId, { viewType ->
    onCreateView(this as CSListRowView<RowType, T>, viewType)
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