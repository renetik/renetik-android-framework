package renetik.android.event.lifecycle

import renetik.android.core.kotlin.primitives.update
import renetik.android.event.CSEvent
import renetik.android.event.change.CSHasChangeValue
import renetik.android.event.change.action
import renetik.android.event.invoke
import renetik.android.event.registration.CSHasRegistrations
import renetik.android.event.registration.plus

fun <T : CSHasDestruct> MutableList<T>.update(
    count: Int, eventAdded: CSEvent<T>? = null,
    eventRemoved: CSEvent<Int>? = null, function: (index: Int) -> T
) = size.update(count,
    onAdd = { index ->
        val item = function(index)
        this += item
        eventAdded?.invoke(item)
    },
    onRemove = { index ->
        removeAt(index).destruct()
            .also { eventRemoved?.invoke(index) }
    })

fun <T : CSHasDestruct> MutableList<T>.factory(
    parent: CSHasRegistrations,
    count: CSHasChangeValue<Int>,
    eventAdded: CSEvent<T>? = null,
    eventRemoved: CSEvent<Int>? = null,
    create: (Int) -> T,
) = apply {
    parent + count.action { count ->
        update(count, eventAdded, eventRemoved, create)
    }
}