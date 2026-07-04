package renetik.android.event.property

import renetik.android.core.kotlin.primitives.update
import renetik.android.core.lang.ArgFun
import renetik.android.core.lang.value.CSValue
import renetik.android.event.lifecycle.CSHasDestruct
import renetik.android.event.lifecycle.destruct

fun CSValue<Int>.update(
    newCount: Int, onAdd: ArgFun<Int>? = null, onRemove: ArgFun<Int>? = null
): Unit = value.update(newCount, onAdd, onRemove)

fun <Item : CSHasDestruct> CSValue<Int>.update(
    list: MutableList<Item>, function: (index: Int) -> Item
): Unit = list.size.update(value,
    onAdd = { index -> list.add(function(index)) },
    onRemove = { index -> list.removeAt(index).destruct() })
