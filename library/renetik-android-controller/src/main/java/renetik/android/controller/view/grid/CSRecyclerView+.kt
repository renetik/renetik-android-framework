package renetik.android.controller.view.grid

import androidx.recyclerview.widget.GridLayoutManager
import renetik.android.controller.view.grid.CSRecyclerView.CSRecyclerViewItem
import renetik.android.core.extensions.content.displayWidth
import renetik.android.core.kotlin.primitives.isEmpty
import renetik.android.core.lang.CSHasTitle
import renetik.android.core.lang.value.CSValue
import renetik.android.event.property.CSProperty
import renetik.android.event.registration.action
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.action
import renetik.android.event.registration.paused
import renetik.android.event.registration.plus

fun <RowType : Any> CSRecyclerView<RowType>.reload(values: Array<out RowType>) =
    reload(values.asIterable())

fun <RowType : Any> CSRecyclerView<RowType>.reload(values: Iterable<RowType>) =
    reload(values.map { CSRecyclerViewItem(it) })

fun <T : CSHasTitle> CSRecyclerView<T>.reload(
    values: Array<T>, search: CSValue<String>, ignoreCase: Boolean = true,
) = reload(values.asIterable(), search, ignoreCase)

fun <T : CSHasTitle> CSRecyclerView<T>.reload(
    values: Iterable<T>, search: CSValue<String>, ignoreCase: Boolean = true,
) = apply {
    val data: Iterable<T> = if (search.value.isEmpty) values
    else values.filter { it.title.contains(search.value, ignoreCase) }
    reload(data.map { CSRecyclerViewItem(it) })
}

val CSRecyclerView<*>.dataCount get() = data.size

fun <T : Any> CSRecyclerView<T>.value(value: T?) = apply { selectedItem.value(value) }

fun <T : Any> CSRecyclerView<T>.property(property: CSProperty<T>) = apply {
    lateinit var propertyRegistration: CSRegistration
    val selectedItemRegistration = selectedItem.onChange { item ->
        propertyRegistration.paused { property.value = item!! }
    }
    propertyRegistration = this + property.action {
        selectedItemRegistration.paused { selectedItem.value(property.value) }
    }
}

@JvmName("propertyNullableItem")
fun <T : Any> CSRecyclerView<T>.property(property: CSProperty<T?>) = apply {
    lateinit var propertyRegistration: CSRegistration
    val selectedItemRegistration = selectedItem.onChange {
        propertyRegistration.paused { property.value = it }
    }
    propertyRegistration = this + property.action {
        selectedItemRegistration.paused { selectedItem.value(property.value) }
    }
    selectedItem.value(property.value)
}

fun <ItemType : Any> CSRecyclerView<ItemType>.sectionGridLayout(
    columnsCount: Int, headerId: Int,
) = apply {
    val layoutManager = GridLayoutManager(this, columnsCount)
    layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            if (isDestructed) return 0
            return if (view.adapter?.getItemViewType(position) == headerId)
                columnsCount else 1
        }
    }
    view.layoutManager = layoutManager
}

fun <ItemType : Any> CSRecyclerView<ItemType>.autoFitGridLayout(columnWidth: Int) = apply {
    view.layoutManager = GridLayoutManager(this, displayWidth / columnWidth)
}

fun <ItemType : Any> CSRecyclerView<ItemType>.columnLayout(columnsCount: Int) = apply {
    view.layoutManager = GridLayoutManager(this, columnsCount)
}


