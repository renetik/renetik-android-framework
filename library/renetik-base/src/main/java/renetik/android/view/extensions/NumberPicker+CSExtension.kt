package renetik.android.view.extensions

import android.widget.NumberPicker
import android.widget.NumberPicker.FOCUS_BEFORE_DESCENDANTS
import android.widget.NumberPicker.FOCUS_BLOCK_DESCENDANTS
import renetik.android.java.extensions.asStringArray
import renetik.android.java.extensions.primitives.count

fun <Row : Any> NumberPicker.loadData(data: List<Row>, selectedIndex: Int) = apply {
    minValue = 1
    displayedValues = data.asStringArray
    maxValue = displayedValues.count
    value = selectedIndex + 1
}

fun NumberPicker.circulate(circulate: Boolean) = apply {
    wrapSelectorWheel = circulate
}

fun NumberPicker.disableTextEditing(disable: Boolean) = apply {
    descendantFocusability = if (disable) FOCUS_BLOCK_DESCENDANTS else FOCUS_BEFORE_DESCENDANTS
}