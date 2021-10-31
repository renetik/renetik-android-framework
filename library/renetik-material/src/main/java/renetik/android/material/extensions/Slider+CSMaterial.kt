package renetik.android.material.extensions

import android.view.View
import com.google.android.material.slider.Slider
import com.google.android.material.slider.Slider.OnChangeListener
import renetik.android.framework.event.CSEventRegistration
import renetik.android.framework.event.property.CSEventProperty
import renetik.android.view.findView

fun View.slider(id: Int) = findView<Slider>(id)!!

fun <T : Slider> T.onChange(listener: (T) -> Unit): CSEventRegistration {
    val sliderListener = OnChangeListener { _, _, _ -> listener(this) }
    addOnChangeListener(sliderListener)
    return object : CSEventRegistration {
        override var isActive = true
            set(value) = if (value) addOnChangeListener(sliderListener)
            else removeOnChangeListener(sliderListener)

        override fun cancel() = removeOnChangeListener(sliderListener)
    }
}

fun <T : Slider> T.onDragStart(listener: (T) -> Unit): CSEventRegistration {
    val sliderListener = object : Slider.OnSliderTouchListener {
        override fun onStartTrackingTouch(slider: Slider) = listener(this@onDragStart)
        override fun onStopTrackingTouch(slider: Slider) = Unit
    }
    addOnSliderTouchListener(sliderListener)
    return object : CSEventRegistration {
        override var isActive = true
            set(value) = if (value) addOnSliderTouchListener(sliderListener)
            else removeOnSliderTouchListener(sliderListener)

        override fun cancel() = removeOnSliderTouchListener(sliderListener)
    }
}

fun <T : Slider> T.onDragStop(listener: (T) -> Unit): CSEventRegistration {
    val sliderListener = object : Slider.OnSliderTouchListener {
        override fun onStartTrackingTouch(slider: Slider) = Unit
        override fun onStopTrackingTouch(slider: Slider) = listener(this@onDragStop)
    }
    addOnSliderTouchListener(sliderListener)
    return object : CSEventRegistration {
        override var isActive = true
            set(value) = if (value) addOnSliderTouchListener(sliderListener)
            else removeOnSliderTouchListener(sliderListener)

        override fun cancel() = removeOnSliderTouchListener(sliderListener)
    }
}

fun <T : Slider> T.value(value: Double) = apply { this.value = value.toFloat() }
fun <T : Slider> T.value(value: Float) = apply { this.value = value }
fun <T : Slider> T.value(value: Int) = apply { this.value = value.toFloat() }
fun <T : Slider> T.valueFrom(value: Float) = apply { this.valueFrom = value }
fun <T : Slider> T.valueFrom(value: Int) = apply { this.valueFrom = value.toFloat() }
fun <T : Slider> T.valueTo(value: Float) = apply { this.valueTo = value }
fun <T : Slider> T.valueTo(value: Int) = apply { this.valueTo = value.toFloat() }
fun <T : Slider> T.stepSize(value: Float) = apply { this.stepSize = value }
fun <T : Slider> T.stepSize(value: Int) = apply { this.stepSize = value.toFloat() }

@JvmName("valuePropertyDouble")
fun Slider.value(property: CSEventProperty<Double>) = apply {
    value(property.value)
    onChange { property.value = it.value.toDouble() }
}

@JvmName("valuePropertyInt")
fun Slider.value(property: CSEventProperty<Int>) = apply {
    value(property.value)
    onChange { property.value = it.value.toInt() }
}