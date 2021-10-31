package renetik.android.widget

import android.graphics.drawable.ColorDrawable
import android.widget.NumberPicker
import android.widget.NumberPicker.FOCUS_BEFORE_DESCENDANTS
import android.widget.NumberPicker.FOCUS_BLOCK_DESCENDANTS
import androidx.annotation.ColorInt
import renetik.android.framework.Func
import renetik.android.framework.event.CSEventRegistration
import renetik.android.framework.event.pause
import renetik.android.framework.event.property.CSEventProperty
import renetik.kotlin.asStringArray
import renetik.kotlin.collections.hasItems
import renetik.kotlin.collections.index
import renetik.kotlin.setPrivateField2

@Deprecated("Impossible to style this widget , use CSNumberPicker")
fun <Row : Any> NumberPicker.loadData(data: List<Row>, selected: Row? = null) =
    loadData(data, selectedIndex = data.index(selected) ?: 0)

@Deprecated("Impossible to style this widget , use CSNumberPicker")
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

@Deprecated("Impossible to style this widget , use CSNumberPicker")
fun NumberPicker.onValueChange(function: Func) = apply {
    setOnValueChangedListener { _, _, _ -> function() }
}

@Deprecated("Impossible to style this widget , use CSNumberPicker")
fun NumberPicker.circulate(circulate: Boolean) = apply {
    wrapSelectorWheel = circulate
}

@Deprecated("Impossible to style this widget , use CSNumberPicker")
fun NumberPicker.disableTextEditing(disable: Boolean) = apply {
    descendantFocusability = if (disable) FOCUS_BLOCK_DESCENDANTS else FOCUS_BEFORE_DESCENDANTS
}

@Deprecated("Impossible to style this widget , use CSNumberPicker")
fun NumberPicker.min(value: Int) = apply { minValue = value }

@Deprecated("Impossible to style this widget , use CSNumberPicker")
fun NumberPicker.max(value: Int) = apply { maxValue = value }

@Deprecated("Impossible to style this widget , use CSNumberPicker")
@JvmName("valuePropertyInt")
fun NumberPicker.value(property: CSEventProperty<Int>): CSEventRegistration {
    val registration = value(property) { it }
    onValueChange { registration.pause().use { property.value = value } }
    return registration
}

@Deprecated("Impossible to style this widget , use CSNumberPicker")
fun <T> NumberPicker.value(property: CSEventProperty<T>, function: (T) -> Int)
        : CSEventRegistration {
    value = function(property.value)
    return property.onChange { value = function(property.value) }
}

@Deprecated("Impossible to style this widget , use CSNumberPicker")
// setSelectionDividerHeight requires api 29
fun NumberPicker.selectionDividerHeight(height: Int) =
    setPrivateField2("mSelectionDividerHeight", height)

@Deprecated("Impossible to style this widget , use CSNumberPicker")
fun NumberPicker.selectionDividerColor(@ColorInt color: Int) =
    setPrivateField2("mSelectionDivider", ColorDrawable(color))