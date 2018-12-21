package renetik.android.listview

import android.widget.AbsListView
import renetik.android.R
import renetik.android.base.layout
import renetik.android.controller.base.CSViewController
import renetik.android.json.data.CSJsonData
import renetik.android.listview.CSListRow.RowTypes
import renetik.android.listview.CSListRow.RowTypes.Row

class CSListRowController<RowType : CSJsonData, T : AbsListView>(
        parent: CSViewController<*>, listViewId: Int,
        private val onCreateView: (CSListRowController<RowType, T>).(Int) -> CSRowView<CSListRow<RowType>>?)
    : CSListController<CSListRow<RowType>, T>(parent, listViewId,
        { viewType ->
            onCreateView(this as CSListRowController<RowType, T>, viewType)
                    ?: CSRowView(this, layout(R.layout.cs_empty))
        }) {

    init {
        viewTypesCount = RowTypes.values().size
        onPositionViewType { position -> dataAt(position)!!.type.ordinal }
        onIsEnabled { position -> Row == dataAt(position)!!.type }
    }

}