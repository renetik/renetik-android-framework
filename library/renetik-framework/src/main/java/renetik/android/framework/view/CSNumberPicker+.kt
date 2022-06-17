package renetik.android.framework.view

import renetik.android.controller.base.view
import renetik.android.core.lang.Func
import renetik.android.event.CSRegistration
import renetik.android.framework.protocol.CSViewInterface
import renetik.android.event.listen
import renetik.android.event.pause
import renetik.android.framework.event.property.CSEventProperty
import renetik.android.core.lang.void
import renetik.android.core.kotlin.asStringArray
import renetik.android.core.kotlin.collections.hasItems
import renetik.android.core.kotlin.collections.index

fun CSViewInterface.numberPicker(viewId: Int) = view(viewId) as CSNumberPicker

@JvmName("valuePropertyInt")
fun CSNumberPicker.value(property: CSEventProperty<Int>): CSRegistration {
    val registration = value(property) { it }
    onValueChange { registration.pause().use { property.value = value } }
    return registration
}

fun <T> CSNumberPicker.value(property: CSEventProperty<T>, function: (T) -> Int)
        : CSRegistration {
    valueProperty.value = function(property.value)
    return property.onChange { valueProperty.value = function(property.value) }
}

fun CSNumberPicker.onValueChange(function: Func) = valueProperty.onChange { function() }

fun CSNumberPicker.onScroll(function: (Int) -> void) = eventOnScroll.listen(function)

fun CSNumberPicker.circulate(circulate: Boolean) = apply {
    wrapSelectorWheel = circulate
}

fun CSNumberPicker.min(value: Int) = apply { minValue = value }

fun CSNumberPicker.max(value: Int) = apply { maxValue = value }

fun <Row : Any> CSNumberPicker.loadData(data: List<Row>, selected: Row? = null) =
    loadData(data, selectedIndex = data.index(selected) ?: 0)

fun CSNumberPicker.disableTextEditing(disable: Boolean) = apply {
    descendantFocusability =
        if (disable) android.widget.NumberPicker.FOCUS_BLOCK_DESCENDANTS else android.widget.NumberPicker.FOCUS_BEFORE_DESCENDANTS
}


fun <Row : Any> CSNumberPicker.loadData(data: List<Row>, selectedIndex: Int = 0) = apply {
    if (data.hasItems) {
        minValue = 0
        valueProperty.value = 0
        val newMaxValue = data.size - 1
        if (newMaxValue > maxValue) {
            displayedValues = data.asStringArray
            maxValue = newMaxValue
        } else {
            maxValue = newMaxValue
            displayedValues = data.asStringArray
        }
        valueProperty.value = selectedIndex
    }
}

