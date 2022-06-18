package renetik.android.ui.extensions.widget

import android.widget.NumberPicker
import android.widget.NumberPicker.FOCUS_BEFORE_DESCENDANTS
import android.widget.NumberPicker.FOCUS_BLOCK_DESCENDANTS
import renetik.android.core.kotlin.asStringArray
import renetik.android.core.kotlin.collections.hasItems
import renetik.android.core.kotlin.collections.index
import renetik.android.core.lang.Func
import renetik.android.event.property.CSEventProperty
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.pause

fun <Row : Any> NumberPicker.loadData(data: List<Row>, selected: Row? = null) =
    loadData(data, selectedIndex = data.index(selected) ?: 0)

fun <Row : Any> NumberPicker.loadData(data: List<Row>, selectedIndex: Int = 0) = apply {
    if (data.hasItems) {
        minValue = 0
        value = 0
        val newMaxValue = data.size - 1
        if (newMaxValue > maxValue) {
            displayedValues = data.asStringArray
            maxValue = newMaxValue
        } else {
            maxValue = newMaxValue
            displayedValues = data.asStringArray
        }
        value = selectedIndex
    }
}

fun NumberPicker.onValueChange(function: Func) = apply {
    setOnValueChangedListener { _, _, _ -> function() }
}

fun NumberPicker.circulate(circulate: Boolean) = apply {
    wrapSelectorWheel = circulate
}

fun NumberPicker.disableTextEditing(disable: Boolean) = apply {
    descendantFocusability = if (disable) FOCUS_BLOCK_DESCENDANTS else FOCUS_BEFORE_DESCENDANTS
}

fun NumberPicker.min(value: Int) = apply { minValue = value }

fun NumberPicker.max(value: Int) = apply { maxValue = value }

@JvmName("valuePropertyInt")
fun NumberPicker.value(property: CSEventProperty<Int>): CSRegistration {
    val registration = value(property) { it }
    onValueChange { registration.pause().use { property.value = value } }
    return registration
}

fun <T> NumberPicker.value(property: CSEventProperty<T>, function: (T) -> Int)
        : CSRegistration {
    value = function(property.value)
    return property.onChange { value = function(property.value) }
}