package renetik.android.controller.view.grid

import renetik.android.framework.lang.CSTitle
import renetik.android.framework.lang.CSValue
import renetik.android.framework.lang.isEmpty

fun <RowType : Any> CSGridView<RowType>.reload(values: Array<out RowType>) =
    reload(values.asIterable())

fun <T : CSTitle> CSGridView<T>.reload(values: Array<T>, searchText: CSValue<String>) =
    reload(values.asIterable(), searchText)

fun <T : CSTitle> CSGridView<T>.reload(values: Iterable<T>, searchText: CSValue<String>) = apply {
    val data = if (searchText.isEmpty) values
    else values.filter { it.title.contains(searchText.value, ignoreCase = true) }
    reload(data)
}

fun <T : Any> CSGridView<T>.value(value: T?) = apply { property.value(value) }