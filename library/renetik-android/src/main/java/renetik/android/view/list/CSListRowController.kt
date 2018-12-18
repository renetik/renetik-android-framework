package renetik.android.view.list

import android.widget.AbsListView
import renetik.android.R
import renetik.android.json.CSJsonData
import renetik.android.view.base.CSViewController
import renetik.android.view.base.layout
import renetik.android.view.list.CSListRow.RowTypes
import renetik.android.view.list.CSListRow.RowTypes.Row

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