package renetik.android.listview.request

import android.widget.ListView
import renetik.android.client.request.CSListServerData
import renetik.android.client.request.CSOperation
import renetik.android.json.data.CSJsonData
import renetik.android.listview.CSListController
import renetik.android.listview.CSListLoadNextController

open class CSRequestListLoadNextController<RowType : CSJsonData, ListType : ListView>(
        parent: CSListController<RowType, ListType>, loadViewLayout: Int
        , onLoadNext: (CSListLoadNextController<RowType, ListType>) -> CSOperation<CSListServerData<RowType>>
) : CSListLoadNextController<RowType, ListType>(parent, loadViewLayout, { loadNextController ->
    onLoadNext(loadNextController).onSuccess { row -> parent.load(row.list) }
})