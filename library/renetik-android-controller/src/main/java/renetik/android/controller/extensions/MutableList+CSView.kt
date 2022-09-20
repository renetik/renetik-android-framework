package renetik.android.controller.extensions

import android.view.ViewGroup
import renetik.android.controller.base.CSView
import renetik.android.core.kotlin.collections.put
import renetik.android.core.kotlin.primitives.update
import renetik.android.event.property.CSProperty
import renetik.android.event.property.action
import renetik.android.event.registration.CSRegistration

fun <Item : CSView<*>> MutableList<Item>.updated(
    group: ViewGroup, property: CSProperty<Int>,
    function: (index: Int) -> Item): CSRegistration =
    property.action { value ->
        size.update(value,
            onAdd = { index -> group.add(put(function(index))) },
            onRemove = { index -> group.remove(removeAt(index)) })
    }
