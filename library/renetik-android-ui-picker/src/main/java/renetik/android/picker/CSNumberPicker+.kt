package renetik.android.picker

import android.widget.NumberPicker.FOCUS_BEFORE_DESCENDANTS
import android.widget.NumberPicker.FOCUS_BLOCK_DESCENDANTS
import com.shawnlin.numberpicker.NumberPicker
import com.shawnlin.numberpicker.NumberPicker.ASCENDING
import renetik.android.core.kotlin.asStringArray
import renetik.android.core.kotlin.collections.hasItems
import renetik.android.core.kotlin.collections.index
import renetik.android.core.lang.Func
import renetik.android.event.property.CSProperty
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.action
import renetik.android.event.registration.paused
import renetik.android.ui.extensions.view
import renetik.android.ui.extensions.view.fadeIn
import renetik.android.ui.extensions.view.invisible
import renetik.android.ui.protocol.CSViewInterface

fun CSViewInterface.numberPicker(viewId: Int) = view(viewId) as CSNumberPicker

fun CSNumberPicker.index(property: CSProperty<Int>): CSRegistration {
    val propertyRegistration = this.index(property) {
        if (order == ASCENDING) it else it + 1
    }
    val valueChangeRegistration = onIndexChange {
        propertyRegistration.paused {
            property.value = if (order == ASCENDING)
                value else value - 1
        }
    }
    return CSRegistration(propertyRegistration, valueChangeRegistration)
}

fun <T> CSNumberPicker.index(property: CSProperty<T>, function: (T) -> Int): CSRegistration =
    property.action { index.value = function(property.value) }

fun CSNumberPicker.onIndexChange(function: Func) = index.onChange { function() }

fun CSNumberPicker.onScroll(function: (Int) -> Unit) = eventOnScroll.listen(function)

fun <T> CSNumberPicker.load(
    property: CSProperty<T>, data: List<Pair<String, T>>
): CSRegistration {
    load(data.map { it.first }, data.indexOfFirst { it.second == property.value })
    return index.onChange { property.value = data[it].second }
}

fun <Row : Any> CSNumberPicker.load(data: List<Row>, selected: Row? = null) =
    load(data, selected = data.index(selected) ?: 0)

fun <Row : Any> CSNumberPicker.load(data: List<Row>, selected: Int = 0) = apply {
    if (data.hasItems) {
        fadeIn()
        minValue = 0
        val newMaxValue = data.size - 1
        if (newMaxValue > maxValue) {
            displayedValues = data.asStringArray
            maxValue = newMaxValue
        } else {
            maxValue = newMaxValue
            displayedValues = data.asStringArray
        }
        index.value = selected
    } else invisible()
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