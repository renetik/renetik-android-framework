package renetik.android.listview.extensions

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import renetik.android.listview.request.CSRequestListController
import renetik.android.view.extensions.onDone
import renetik.android.view.extensions.onRefresh

fun SwipeRefreshLayout.listController(listController: CSRequestListController<*, *>) {
    onRefresh { pull -> listController.reload(false).refresh().onDone { pull.onDone() } }
}