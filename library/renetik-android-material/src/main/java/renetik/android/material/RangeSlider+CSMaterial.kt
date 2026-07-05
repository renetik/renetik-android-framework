package renetik.android.material

import android.view.View
import com.google.android.material.slider.RangeSlider
import renetik.android.ui.view.findView

fun View.rangeSlider(id: Int) = findView<RangeSlider>(id)!!

fun <T : RangeSlider> T.onChange(listener: (T) -> Unit) = apply {
    addOnChangeListener { _, _, _ -> listener(this) }
}