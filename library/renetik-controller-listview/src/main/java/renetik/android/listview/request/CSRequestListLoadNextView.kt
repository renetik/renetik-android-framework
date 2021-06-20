package renetik.android.listview.request

import android.widget.ListView
import renetik.android.client.request.CSListServerData
import renetik.android.client.request.CSOperation
import renetik.android.json.data.CSJsonMap
import renetik.android.listview.CSListView
import renetik.android.listview.CSListLoadNextView

open class CSRequestListLoadNextView<RowType : CSJsonMap, ListType : ListView>(
    parent: CSListView<RowType, ListType>, loadViewLayout: Int
    , onLoadNext: (CSListLoadNextView<RowType, ListType>) -> CSOperation<CSListServerData<RowType>>
) : CSListLoadNextView<RowType, ListType>(parent, loadViewLayout, { loadNextController ->
    onLoadNext(loadNextController).onSuccess { row -> parent.load(row.list) }
})