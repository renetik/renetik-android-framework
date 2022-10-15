package renetik.android.ui.extensions

import android.view.ViewGroup
import renetik.android.core.kotlin.collections.put
import renetik.android.core.kotlin.primitives.update
import renetik.android.event.property.CSProperty
import renetik.android.event.property.action
import renetik.android.event.registration.CSRegistration
import renetik.android.ui.protocol.CSViewInterface

fun <Item : CSViewInterface> CSProperty<Int>.updates(
    items: MutableList<Item>, layout: ViewGroup, fromStart: Boolean = false,
    function: (index: Int) -> Item): CSRegistration = action { value ->
    items.size.update(value,
        onAdd = { index -> layout.add(items.put(function(index)), if (fromStart) 0 else -1) },
        onRemove = { index ->
            val view = items.removeAt(index)
            if (!view.isDestroyed) layout.remove(view)
        })
}
