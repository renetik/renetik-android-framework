package renetik.android.controller.view.grid

import renetik.android.controller.view.grid.item.CSGridItemView
import renetik.android.core.kotlin.primitives.isBlank
import renetik.android.core.lang.CSHasTitle
import renetik.android.core.lang.value.CSValue
import renetik.android.event.property.CSProperty
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.action
import renetik.android.event.registration.paused
import renetik.android.event.registration.plus

fun <RowType : Any, ViewType : CSGridItemView<RowType>>
        CSGridView<RowType, ViewType>.reload(values: Array<out RowType>) =
    reload(values.asIterable())

fun <RowType : Any>
        CSGridView<RowType, out CSGridItemView<RowType>>.reload(
    values: Iterable<RowType>
) = reload(values.map { it to 0 })

fun <T : CSHasTitle> GridViewOut<T>.reload(
    values: Array<T>, search: CSValue<String>, ignoreCase: Boolean = true,
) = reload(values.asIterable(), search, ignoreCase)

fun <T : CSHasTitle> GridViewOut<T>.reload(
    values: Iterable<T>, search: CSValue<String>, ignoreCase: Boolean = true,
) = apply {
    val data: Iterable<T> = if (search.value.isBlank) values
    else values.filter { it.title.contains(search.value, ignoreCase) }
    reload(data.map { it to 0 })
}

val GridViewOut<*>.dataCount get() = data.size

fun <T : Any> GridViewOut<T>.value(value: T?) = apply { selectedItem.value(value) }

fun <T : Any> GridViewOut<T>.property(property: CSProperty<T>) = apply {
    lateinit var propertyRegistration: CSRegistration
    val selectedItemRegistration = selectedItem.onChange { item ->
        propertyRegistration.paused { property.value = item!! }
    }
    propertyRegistration = this + property.action {
        selectedItemRegistration.paused { selectedItem.value(property.value) }
    }
}

@JvmName("propertyNullableItem")
fun <T : Any> GridViewOut<T>.property(property: CSProperty<T?>) = apply {
    lateinit var propertyRegistration: CSRegistration
    val selectedItemRegistration = selectedItem.onChange {
        propertyRegistration.paused { property.value = it }
    }
    propertyRegistration = this + property.action {
        selectedItemRegistration.paused { selectedItem.value(property.value) }
    }
    selectedItem.value(property.value)
}


