package renetik.android.material.extensions

import android.view.View
import com.google.android.material.slider.Slider
import com.google.android.material.slider.Slider.OnChangeListener
import renetik.android.core.kotlin.primitives.max
import renetik.android.core.kotlin.primitives.min
import renetik.android.core.kotlin.primitives.roundToStep
import renetik.android.event.property.CSProperty
import renetik.android.event.registration.CSHasChangeValue
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.CSRegistration.Companion.CSRegistration
import renetik.android.event.registration.paused
import renetik.android.event.registration.start
import renetik.android.ui.extensions.view.findView
import kotlin.math.roundToInt

class CSSlider(val slider: Slider) : CSHasChangeValue<Float> {
    override val value: Float get() = slider.value

    override fun onChange(function: (Float) -> Unit): CSRegistration =
        slider.onChange { function(value) }
}

fun View.slider(id: Int) = findView<Slider>(id)!!

fun <T : Slider> T.onChange(listener: (T) -> Unit): CSRegistration {
    val sliderListener = OnChangeListener { _, _, _ -> listener(this) }
    return CSRegistration(onResume = { addOnChangeListener(sliderListener) },
        onPause = { removeOnChangeListener(sliderListener) }).start()
}

fun <T : Slider> T.onDragStart(listener: (T) -> Unit): CSRegistration {
    val sliderListener = object : Slider.OnSliderTouchListener {
        override fun onStartTrackingTouch(slider: Slider) = listener(this@onDragStart)
        override fun onStopTrackingTouch(slider: Slider) = Unit
    }
    return CSRegistration(onResume = { addOnSliderTouchListener(sliderListener) },
        onPause = { removeOnSliderTouchListener(sliderListener) }).start()
}

fun <T : Slider> T.onDragStop(listener: (T) -> Unit): CSRegistration {
    val sliderListener = object : Slider.OnSliderTouchListener {
        override fun onStartTrackingTouch(slider: Slider) = Unit
        override fun onStopTrackingTouch(slider: Slider) = listener(this@onDragStop)
    }
    return CSRegistration(onResume = { addOnSliderTouchListener(sliderListener) },
        onPause = { removeOnSliderTouchListener(sliderListener) }).start()
}

fun <T : Slider> T.value(value: Float) =
    apply { this.value = value.min(valueFrom).max(valueTo) }

fun <T : Slider> T.value(value: Int) = apply { this.value(value.toFloat()) }

fun <T : Slider> T.valueFrom(value: Float) = apply { valueFrom = value }
fun <T : Slider> T.valueFrom(value: Int) = apply { valueFrom = value.toFloat() }

fun <T : Slider> T.valueTo(value: Float) = apply { valueTo = value }
fun <T : Slider> T.valueTo(value: Int) = apply { valueTo = value.toFloat() }

fun <T : Slider> T.stepSize(value: Float) = apply { stepSize = value }
fun <T : Slider> T.stepSize(value: Int) = apply { stepSize = value.toFloat() }

@JvmName("valuePropertyDouble")
fun Slider.value(property: CSProperty<Float>,
                 min: Float = 0f, max: Float = 1.0f, step: Float = 0.1f): CSRegistration {
    valueFrom = min
    valueTo = max
    stepSize = step
    value(property.value.roundToStep(step))
    lateinit var onSliderChangeRegistration: CSRegistration
    val onChangeRegistration = property.onChange {
        onSliderChangeRegistration.paused { value(property.value.roundToStep(step)) }
    }
    onSliderChangeRegistration = onChange {
        onChangeRegistration.paused { property.value = value }
    }
    return CSRegistration(onChangeRegistration, onSliderChangeRegistration)
}


@JvmName("valuePropertyInt")
fun Slider.value(property: CSProperty<Int>,
                 min: Int = 0, max: Int = 100, step: Int = 1): CSRegistration {
    valueFrom = min.toFloat()
    valueTo = max.toFloat()
    stepSize = step.toFloat()
    value(property.value.roundToStep(step))
    lateinit var sliderOnChangeRegistration: CSRegistration
    val propertyOnChangeRegistration = property.onChange {
        sliderOnChangeRegistration.paused { value(property.value.roundToStep(step)) }
    }
    sliderOnChangeRegistration = onChange {
        propertyOnChangeRegistration.paused { property.value = value.roundToInt() }
    }
    return CSRegistration(propertyOnChangeRegistration, sliderOnChangeRegistration)
}