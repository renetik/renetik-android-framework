package renetik.android.ui.extensions

import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import renetik.android.core.kotlin.collections.put
import renetik.android.core.kotlin.primitives.update
import renetik.android.event.property.CSProperty
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.action
import renetik.android.ui.protocol.CSViewInterface

fun <ItemView : CSViewInterface> CSProperty<Int>.updates(
    items: MutableList<ItemView>,
    layout: ViewGroup, fromStart: Boolean = false,
    layoutParams: LayoutParams? = null,
    createView: (index: Int) -> ItemView
): CSRegistration = action { value ->
    items.size.update(value,
        onAdd = { index ->
            val view = items.put(createView(index))
            val addIndex = if (fromStart) 0 else -1
            layoutParams?.let { layout.add(view = view, params = it, index = addIndex) }
                ?: layout.add(view = view, index = addIndex)
        },
        onRemove = { index ->
            val itemView: ItemView = items.removeAt(index)
            if (!itemView.isDestructed) layout.remove(itemView)
        })
}

fun <ItemView : CSViewInterface> MutableList<ItemView>.updates(
    count: Int, layout: ViewGroup, fromStart: Boolean = false,
    layoutParams: LayoutParams? = null,
    createView: (index: Int) -> ItemView
) = size.update(count,
    onAdd = { index ->
        val view = put(createView(index))
        val addIndex = if (fromStart) 0 else -1
        layoutParams?.let { layout.add(view = view, params = it, index = addIndex) }
            ?: layout.add(view = view, index = addIndex)
    },
    onRemove = { index ->
        val itemView: ItemView = removeAt(index)
        if (!itemView.isDestructed) layout.remove(itemView)
    }
)
