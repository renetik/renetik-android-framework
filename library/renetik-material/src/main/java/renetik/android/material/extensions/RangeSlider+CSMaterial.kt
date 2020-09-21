package renetik.android.material.extensions

import android.view.View
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider
import renetik.android.view.extensions.findView

fun View.rangeSlider(id: Int) = findView<RangeSlider>(id)!!

fun <T : RangeSlider> T.onChange(listener: (T) -> Unit) = apply {
    addOnChangeListener { _, _, _ -> listener(this) }
}

fun <T : Slider> T.valueFrom(value: Float) = apply { this.valueFrom = value }

fun <T : Slider> T.valueTo(value: Int) = apply { this.valueTo = value.toFloat() }