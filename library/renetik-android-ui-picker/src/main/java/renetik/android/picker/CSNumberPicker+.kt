package renetik.android.picker

import android.widget.NumberPicker.FOCUS_BEFORE_DESCENDANTS
import android.widget.NumberPicker.FOCUS_BLOCK_DESCENDANTS
import com.shawnlin.numberpicker.NumberPicker
import renetik.android.controller.base.view
import renetik.android.core.kotlin.asStringArray
import renetik.android.core.kotlin.collections.hasItems
import renetik.android.core.kotlin.collections.index
import renetik.android.core.lang.Func
import renetik.android.core.lang.void
import renetik.android.event.listen
import renetik.android.event.property.CSEventProperty
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.pause
import renetik.android.ui.protocol.CSViewInterface

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

fun <Row : Any> CSNumberPicker.loadData(data: List<Row>, selected: Row? = null) =
    loadData(data, selectedIndex = data.index(selected) ?: 0)

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

fun <T : NumberPicker> T.circulate(circulate: Boolean) = apply {
    wrapSelectorWheel = circulate
}

fun <T : NumberPicker> T.min(value: Int) = apply { minValue = value }

fun <T : NumberPicker> T.max(value: Int) = apply { maxValue = value }

fun <T : NumberPicker> T.disableTextEditing(disable: Boolean) = apply {
    descendantFocusability = if (disable) FOCUS_BLOCK_DESCENDANTS
    else FOCUS_BEFORE_DESCENDANTS
}