package renetik.android.controller.view.grid

import androidx.recyclerview.widget.GridLayoutManager
import renetik.android.core.kotlin.primitives.isEmpty
import renetik.android.core.lang.CSHasTitle
import renetik.android.core.lang.value.CSValue
import renetik.android.event.property.CSProperty
import renetik.android.event.property.action
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.paused
import renetik.android.event.registration.register

//fun <RowType : Any> CSGridView<RowType>.reload(values: Array<out RowType>) =
//    reload(values.asIterable())
//
//fun <T : CSHasTitle> CSGridView<T>.reload(values: Array<T>, searchText: CSValue<String>) =
//    reload(values.asIterable(), searchText)
//
//fun <T : CSHasTitle> CSGridView<T>.reload(
//    values: Iterable<T>, searchText: CSValue<String>) = apply {
//    val data = if (searchText.value.isEmpty) values
//    else values.filter { it.title.contains(searchText.value, ignoreCase = true) }
//    reload(data)
//}

val CSRecyclerView<*>.dataCount get() = data.size

fun <T : Any> CSRecyclerView<T>.value(value: T?) = apply { selectedItem.value(value) }

fun <T : Any> CSRecyclerView<T>.property(property: CSProperty<T>) = apply {
    lateinit var propertyRegistration: CSRegistration
    val selectedItemRegistration = selectedItem.onChange { item ->
        propertyRegistration.paused { property.value = item!! }
    }
    propertyRegistration = register(property.action {
        selectedItemRegistration.paused { selectedItem.value(property.value) }
    })
}

@JvmName("propertyNullableItem")
fun <T : Any> CSRecyclerView<T>.property(property: CSProperty<T?>) = apply {
    lateinit var propertyRegistration: CSRegistration
    val selectedItemRegistration = selectedItem.onChange {
        propertyRegistration.paused { property.value = it }
    }
    propertyRegistration = register(property.action {
        selectedItemRegistration.paused { selectedItem.value(property.value) }
    })
    selectedItem.value(property.value)
}

fun <ItemType : Any> CSRecyclerView<ItemType>.sectionGridLayout(
    columnsCount: Int, headerViewId: Int) = apply {
    val layoutManager = GridLayoutManager(this, columnsCount)
    layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int =
            if (view.adapter?.getItemViewType(position) == headerViewId)
                columnsCount else 1
    }
    view.layoutManager = layoutManager
}