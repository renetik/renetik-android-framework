package renetik.android.framework.view

import com.shawnlin.numberpicker.NumberPicker
import renetik.android.controller.base.view
import renetik.android.framework.Func
import renetik.android.framework.event.CSEventRegistration
import renetik.android.framework.event.CSViewInterface
import renetik.android.framework.event.pause
import renetik.android.framework.event.property.CSEventProperty
import renetik.kotlin.asStringArray
import renetik.kotlin.collections.hasItems
import renetik.kotlin.collections.index

fun CSViewInterface.numberPicker(viewId: Int) = view(viewId) as NumberPicker

@JvmName("valuePropertyInt")
fun NumberPicker.value(property: CSEventProperty<Int>): CSEventRegistration {
    val registration = value(property) { it }
    onValueChange { registration.pause().use { property.value = value } }
    return registration
}

fun <T> NumberPicker.value(property: CSEventProperty<T>, function: (T) -> Int)
        : CSEventRegistration {
    value = function(property.value)
    return property.onChange { value = function(property.value) }
}

fun NumberPicker.onValueChange(function: Func) = apply {
    setOnValueChangedListener { _, _, _ -> function() }
}

fun NumberPicker.circulate(circulate: Boolean) = apply {
    wrapSelectorWheel = circulate
}

fun NumberPicker.min(value: Int) = apply { minValue = value }

fun NumberPicker.max(value: Int) = apply { maxValue = value }

fun <Row : Any> NumberPicker.loadData(data: List<Row>, selected: Row? = null) =
    loadData(data, selectedIndex = data.index(selected) ?: 0)

fun NumberPicker.disableTextEditing(disable: Boolean) = apply {
    descendantFocusability =
        if (disable) android.widget.NumberPicker.FOCUS_BLOCK_DESCENDANTS else android.widget.NumberPicker.FOCUS_BEFORE_DESCENDANTS
}


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