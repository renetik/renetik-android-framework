package renetik.android.controller.view.grid

import renetik.android.event.registration.pause
import renetik.android.event.property.CSProperty
import renetik.android.event.registration.register
import renetik.android.event.registration.resume
import renetik.android.core.lang.CSHasTitle
import renetik.android.core.lang.value.CSValue
import renetik.android.core.lang.value.isEmpty

fun <RowType : Any> CSGridView<RowType>.reload(values: Array<out RowType>) =
    reload(values.asIterable())

fun <T : CSHasTitle> CSGridView<T>.reload(values: Array<T>, searchText: CSValue<String>) =
    reload(values.asIterable(), searchText)

fun <T : CSHasTitle> CSGridView<T>.reload(values: Iterable<T>, searchText: CSValue<String>) =
    apply {
        val data = if (searchText.isEmpty) values
        else values.filter { it.title.contains(searchText.value, ignoreCase = true) }
        reload(data)
    }

val CSGridView<*>.dataCount get() = data.size

fun <T : Any> CSGridView<T>.value(value: T?) = apply { selectedItem.value(value) }

fun <T : Any> CSGridView<T>.property(property: CSProperty<T>) = apply {
    val registration = register(property.onChange { selectedItem.value(property.value) })
    selectedItem.value(property.value)
    selectedItem.onChange { item -> registration.pause().use { property.value = item!! } }
}

@JvmName("propertyNullableItem")
fun <T : Any> CSGridView<T>.property(property: CSProperty<T?>) = apply {
    val registration = parent.register(property
        .onChange { selectedItem.value(property.value) })
    selectedItem.onChange {
        registration.pause()
        property.value = it
        registration.resume()
    }
    selectedItem.value(property.value)
}