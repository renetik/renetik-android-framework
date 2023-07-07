package renetik.android.framework.extensions

import android.view.ViewGroup
import renetik.android.core.kotlin.collections.put
import renetik.android.core.kotlin.primitives.update
import renetik.android.event.property.CSProperty
import renetik.android.event.registration.CSRegistration
import renetik.android.preset.Preset
import renetik.android.preset.extensions.action
import renetik.android.ui.extensions.add
import renetik.android.ui.extensions.remove
import renetik.android.ui.protocol.CSViewInterface

fun <ItemView : CSViewInterface> CSProperty<Int>.updates(
    preset: Preset,
    items: MutableList<ItemView>,
    layout: ViewGroup, fromStart: Boolean = false,
    layoutParams: ViewGroup.LayoutParams? = null,
    createView: (index: Int) -> ItemView
): CSRegistration = action(preset) { value ->
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