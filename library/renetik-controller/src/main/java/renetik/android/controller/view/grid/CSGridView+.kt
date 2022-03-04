package renetik.android.controller.view.grid

import renetik.android.framework.event.pause
import renetik.android.framework.event.property.CSEventProperty
import renetik.android.framework.event.register
import renetik.android.framework.event.resume
import renetik.android.framework.lang.CSHasTitle
import renetik.android.framework.lang.CSValue
import renetik.android.framework.lang.isEmpty

fun <RowType : Any> CSGridView<RowType>.reload(values: Array<out RowType>) =
    reload(values.asIterable())

fun <T : CSHasTitle> CSGridView<T>.reload(values: Array<T>, searchText: CSValue<String>) =
    reload(values.asIterable(), searchText)

fun <T : CSHasTitle> CSGridView<T>.reload(values: Iterable<T>, searchText: CSValue<String>) = apply {
    val data = if (searchText.isEmpty) values
    else values.filter { it.title.contains(searchText.value, ignoreCase = true) }
    reload(data)
}

val CSGridView<*>.dataCount get() = data.size

fun <T : Any> CSGridView<T>.value(value: T?) = apply { selectedItem.value(value) }

fun <T : Any> CSGridView<T>.property(property: CSEventProperty<T>) = apply {
    val registration = register(property.onChange {
        selectedItem.value(property.value)
    })
    selectedItem.onChange {
        registration.pause()
        property.value = it!!
        registration.resume()
    }
    selectedItem.value(property.value)
}

@JvmName("propertyNullableItem")
fun <T : Any> CSGridView<T>.property(property: CSEventProperty<T?>) = apply {
    val registration = parent.register(property
        .onChange { selectedItem.value(property.value) })
    selectedItem.onChange {
        registration.pause()
        property.value = it
        registration.resume()
    }
    selectedItem.value(property.value)
}