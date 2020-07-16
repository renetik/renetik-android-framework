package renetik.android.view.extensions

import android.widget.NumberPicker
import renetik.android.java.common.CSName
import renetik.android.java.common.asStringArray
import renetik.android.java.extensions.primitives.count

fun <Row : CSName> NumberPicker.loadData(data: List<Row>, selectedIndex: Int) = apply {
    minValue = 1
    displayedValues = data.asStringArray
    maxValue = displayedValues.count
    value = selectedIndex + 1
}

fun NumberPicker.circulate(circulate: Boolean) = apply {
    wrapSelectorWheel = circulate
}