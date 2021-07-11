package renetik.android.listview.extensions

import android.widget.AbsListView
import renetik.android.listview.CSListView

fun <RowType : Any, ViewType : AbsListView>
        CSListView<RowType, ViewType>.reload(array: Array<out RowType>) = reload(array.toList())