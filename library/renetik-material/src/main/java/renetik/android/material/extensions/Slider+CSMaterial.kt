package renetik.android.material.extensions

import android.view.View
import com.google.android.material.slider.Slider
import renetik.android.framework.event.property.CSEventProperty
import renetik.android.view.extensions.findView

fun View.slider(id: Int) = findView<Slider>(id)!!

fun <T : Slider> T.onChange(listener: (T) -> Unit) = apply {
    addOnChangeListener { _, _, _ -> listener(this) }
}

fun <T : Slider> T.onDragStop(listener: (T) -> Unit) = apply {
    addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
        override fun onStartTrackingTouch(slider: Slider) {
        }

        override fun onStopTrackingTouch(slider: Slider) {
            listener(this@apply)
        }
    })
}

fun <T : Slider> T.value(value: Float) = apply { this.value = value }
fun <T : Slider> T.value(value: Int) = apply { this.value = value.toFloat() }
fun <T : Slider> T.valueFrom(value: Float) = apply { this.valueFrom = value }
fun <T : Slider> T.valueFrom(value: Int) = apply { this.valueFrom = value.toFloat() }
fun <T : Slider> T.valueTo(value: Float) = apply { this.valueTo = value }
fun <T : Slider> T.valueTo(value: Int) = apply { this.valueTo = value.toFloat() }
fun <T : Slider> T.stepSize(value: Float) = apply { this.stepSize = value }
fun <T : Slider> T.stepSize(value: Int) = apply { this.stepSize = value.toFloat() }

fun Slider.value(property: CSEventProperty<Double>) = apply {
    value = property.value.toFloat()
    onChange { property.value = it.value.toDouble() }
}