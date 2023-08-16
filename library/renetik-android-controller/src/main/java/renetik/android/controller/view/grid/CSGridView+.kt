package renetik.android.controller.view.grid

import renetik.android.core.kotlin.primitives.isEmpty
import renetik.android.core.lang.CSHasTitle
import renetik.android.core.lang.value.CSValue
import renetik.android.core.lang.variable.CSVariable
import renetik.android.event.property.CSProperty
import renetik.android.event.registration.CSHasChangeValue.Companion.action
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.paused
import renetik.android.event.registration.plus

fun <RowType : Any> CSGridView<RowType>.reload(values: Array<out RowType>) =
    reload(values.asIterable())

fun <T : CSHasTitle> CSGridView<T>.reload(values: Array<T>, searchText: CSValue<String>) =
    reload(values.asIterable(), searchText)

fun <T : CSHasTitle> CSGridView<T>.reload(
    values: Iterable<T>, searchText: CSValue<String>
) = apply {
    val data = if (searchText.value.isEmpty) values
    else values.filter { it.title.contains(searchText.value, ignoreCase = true) }
    reload(data)
}

val CSGridView<*>.dataCount get() = data.size

fun <T : Any> CSGridView<T>.value(value: T?) = apply { selectedItem.value(value) }

fun <T : Any> CSGridView<T>.variable(variable: CSVariable<T>) = apply {
    value(variable.value)
    onItemSelected { variable.value = it.value }
}

fun <T : Any> CSGridView<T>.property(property: CSProperty<T>) = apply {
    lateinit var propertyRegistration: CSRegistration
    val selectedItemRegistration = selectedItem.onChange { item ->
        propertyRegistration.paused { property.value = item!! }
    }
    propertyRegistration = this + property.action {
        selectedItemRegistration.paused { selectedItem.value(property.value) }
    }
}

@JvmName("propertyNullableItem")
fun <T : Any> CSGridView<T>.property(property: CSProperty<T?>) = apply {
    lateinit var propertyRegistration: CSRegistration
    val selectedItemRegistration = selectedItem.onChange {
        propertyRegistration.paused { property.value = it }
    }
    propertyRegistration = this + property.action {
        selectedItemRegistration.paused { selectedItem.value(property.value) }
    }
    selectedItem.value(property.value)
}