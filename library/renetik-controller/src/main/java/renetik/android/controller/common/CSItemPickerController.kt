package renetik.android.controller.common

import android.view.Gravity.CENTER
import android.view.View
import android.widget.FrameLayout
import android.widget.FrameLayout.LayoutParams
import android.widget.FrameLayout.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout.LayoutParams.WRAP_CONTENT
import android.widget.NumberPicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import renetik.android.controller.R
import renetik.android.controller.base.CSViewController
import renetik.android.extensions.toPixel
import renetik.android.java.common.CSName
import renetik.android.java.common.asStringArray
import renetik.android.java.event.CSEventProperty
import renetik.android.java.extensions.primitives.count
import renetik.android.view.extensions.padding

class CSItemPickerController<Row : CSName>(
    title: CharSequence, data: List<Row>, property: CSEventProperty<Row?>) :
    CSViewController<View>(navigation) {

    companion object Factory

    init {
        val picker = NumberPicker(this)
        picker.minValue = 1
        picker.displayedValues = data.asStringArray
        picker.maxValue = picker.displayedValues.count
        val layout = FrameLayout(this)
        layout.addView(picker, LayoutParams(MATCH_PARENT, WRAP_CONTENT, CENTER))
        layout.padding(toPixel(5))
        MaterialAlertDialogBuilder(this).setView(layout).setTitle(title)
            .setPositiveButton(getString(R.string.renetik_android_base_yes)) { _, _ ->
                property.value = data[picker.value - 1]
            }.show()
    }
}

