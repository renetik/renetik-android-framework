package renetik.android.listview.request

import android.widget.AbsListView
import renetik.android.client.request.CSListServerData
import renetik.android.client.request.CSOperation
import renetik.android.controller.base.CSViewController
import renetik.android.json.data.CSJsonData
import renetik.android.listview.CSListController
import renetik.android.listview.CSRowView

open class CSRequestListController<RowType : CSJsonData, ViewType : AbsListView>
    : CSListController<RowType, ViewType> {

    var onReload: ((progress: Boolean) -> CSOperation<CSListServerData<RowType>>)? = null

    constructor(parent: CSViewController<*>, view: ViewType,
                createView: (CSListController<RowType, ViewType>).(Int) -> CSRowView<RowType>)
            : super(parent, view, createView) {
    }

    constructor(parent: CSViewController<*>, listViewId: Int,
                createView: (CSListController<RowType, ViewType>).(Int) -> CSRowView<RowType>)
            : super(parent, listViewId, createView) {
    }

    fun reload(progress: Boolean) = onReload!!(progress).onSuccess { reload(it.list) }
}

fun <RowType : CSJsonData, ListControllerType : CSRequestListController<RowType, *>>
        ListControllerType.onReload(function: (progress: Boolean) -> CSOperation<CSListServerData<RowType>>) =
        apply { onReload = function }
