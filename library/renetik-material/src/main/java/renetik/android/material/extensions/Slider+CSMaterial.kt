package renetik.android.material.extensions

import android.view.View
import com.google.android.material.slider.Slider
import com.google.android.material.slider.Slider.OnChangeListener
import renetik.android.framework.event.CSRegistration
import renetik.android.framework.event.CSMultiEventRegistration
import renetik.android.framework.event.pause
import renetik.android.framework.event.property.CSEventProperty
import renetik.android.primitives.roundToStep
import renetik.android.view.findView
import kotlin.math.roundToInt

fun View.slider(id: Int) = findView<Slider>(id)!!

fun <T : Slider> T.onChange(listener: (T) -> Unit): CSRegistration {
    val sliderListener = OnChangeListener { _, _, _ -> listener(this) }
    addOnChangeListener(sliderListener)
    return object : CSRegistration {
        override var isActive = true
            set(value) = if (value) addOnChangeListener(sliderListener)
            else removeOnChangeListener(sliderListener)

        override fun cancel() = removeOnChangeListener(sliderListener)
    }
}

fun <T : Slider> T.onDragStart(listener: (T) -> Unit): CSRegistration {
    val sliderListener = object : Slider.OnSliderTouchListener {
        override fun onStartTrackingTouch(slider: Slider) = listener(this@onDragStart)
        override fun onStopTrackingTouch(slider: Slider) = Unit
    }
    addOnSliderTouchListener(sliderListener)
    return object : CSRegistration {
        override var isActive = true
            set(value) = if (value) addOnSliderTouchListener(sliderListener)
            else removeOnSliderTouchListener(sliderListener)

        override fun cancel() = removeOnSliderTouchListener(sliderListener)
    }
}

fun <T : Slider> T.onDragStop(listener: (T) -> Unit): CSRegistration {
    val sliderListener = object : Slider.OnSliderTouchListener {
        override fun onStartTrackingTouch(slider: Slider) = Unit
        override fun onStopTrackingTouch(slider: Slider) = listener(this@onDragStop)
    }
    addOnSliderTouchListener(sliderListener)
    return object : CSRegistration {
        override var isActive = true
            set(value) = if (value) addOnSliderTouchListener(sliderListener)
            else removeOnSliderTouchListener(sliderListener)

        override fun cancel() = removeOnSliderTouchListener(sliderListener)
    }
}

fun <T : Slider> T.value(value: Float) = apply { this.value = value }
fun <T : Slider> T.value(value: Int) = apply { this.value = value.toFloat() }
fun <T : Slider> T.valueFrom(value: Float) = apply { this.valueFrom = value }
fun <T : Slider> T.valueFrom(value: Int) = apply { this.valueFrom = value.toFloat() }
fun <T : Slider> T.valueTo(value: Float) = apply { this.valueTo = value }
fun <T : Slider> T.valueTo(value: Int) = apply { this.valueTo = value.toFloat() }
fun <T : Slider> T.stepSize(value: Float) = apply { this.stepSize = value }
fun <T : Slider> T.stepSize(value: Int) = apply { this.stepSize = value.toFloat() }


@JvmName("valuePropertyDouble")
fun Slider.value(property: CSEventProperty<Float>,
                 min: Float = 0f, max: Float = 1.0f, step: Float = 0.1f): CSRegistration {
    valueFrom = min
    valueTo = max
    stepSize = step
    value(property.value.roundToStep(step))
    lateinit var onSliderChangeRegistration: CSRegistration
    val onChangeRegistration = property.onChange {
        onSliderChangeRegistration.pause().use { value(property.value.roundToStep(step)) }
    }
    onSliderChangeRegistration = onChange {
        onChangeRegistration.pause().use { property.value = value }
    }
    return CSMultiEventRegistration(onChangeRegistration, onSliderChangeRegistration)
}


@JvmName("valuePropertyInt")
fun Slider.value(property: CSEventProperty<Int>,
                 min: Int = 0, max: Int = 100, step: Int = 1): CSRegistration {
    valueFrom = min.toFloat()
    valueTo = max.toFloat()
    stepSize = step.toFloat()
    value(property.value.roundToStep(step))
    lateinit var onSliderChangeRegistration: CSRegistration
    val onChangeRegistration = property.onChange {
        onSliderChangeRegistration.pause().use { value(property.value.roundToStep(step)) }
    }
    onSliderChangeRegistration = onChange {
        onChangeRegistration.pause().use { property.value = value.roundToInt() }
    }
    return CSMultiEventRegistration(onChangeRegistration, onSliderChangeRegistration)
}