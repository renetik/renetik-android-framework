package renetik.android.listview

fun <RowType : Any> CSGridView<RowType>.reload(array: Array<out RowType>) = reload(array.toList())