package renetik.android.view.list

import android.widget.AbsListView
import renetik.android.R
import renetik.android.json.CSJsonData
import renetik.android.view.list.CSListRow.RowTypes
import renetik.android.view.list.CSListRow.RowTypes.Row
import renetik.android.view.base.CSViewController
import renetik.android.view.base.layout

class CSListRowController<RowType : CSJsonData, T : AbsListView>(
        parent: CSViewController<*>, listViewId: Int,
        private val onCreateView: (Int, CSListRowController<RowType, T>) -> CSRowView<CSListRow<RowType>>?)
    : CSListController<CSListRow<RowType>, T>(parent, listViewId) {

    init {
        viewTypesCount = RowTypes.values().size
        onPositionViewType { position -> dataAt(position)!!.type.ordinal }
        onIsEnabled { position -> Row == dataAt(position)!!.type }
        onCreateView { viewType ->
            onCreateView(viewType, this) ?: CSRowView(this, layout(R.layout.cs_empty))
        }
    }
}