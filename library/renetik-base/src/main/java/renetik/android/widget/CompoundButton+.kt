package renetik.android.widget

import android.content.res.ColorStateList
import android.widget.CompoundButton
import androidx.annotation.ColorInt
import renetik.android.framework.event.CSEventRegistration
import renetik.android.framework.event.pause
import renetik.android.framework.event.property.CSEventProperty
import renetik.android.framework.lang.isTrue

fun CompoundButton.onChecked(function: (CompoundButton) -> Unit) = apply {
    setOnCheckedChangeListener { buttonView, _ -> function(buttonView) }
}

fun CompoundButton.buttonTint(@ColorInt value: Int?) = apply {
    buttonTintList = if (value != null) ColorStateList.valueOf(value) else null
}

fun CompoundButton.isCheckedIf(condition: Boolean) = apply {
    isChecked = condition
}

fun CompoundButton.isCheckedIf(property: CSEventProperty<Boolean>): CSEventRegistration {
    val onChangeRegistration = property.onChange(this::isCheckedIf)
    onChecked { onChangeRegistration.pause().use { property.value(isChecked) } }
    isCheckedIf(property.isTrue)
    return onChangeRegistration
}

fun <T> CompoundButton.isCheckedIf(property1: CSEventProperty<T>, property2: CSEventProperty<*>,
                                   condition: (T) -> Boolean) =
    isCheckedIf(property1, property2) { first, _ -> condition(first) }

fun <T, V> CompoundButton.isCheckedIf(property1: CSEventProperty<T>, property2: CSEventProperty<V>,
                                      condition: (T, V) -> Boolean): CSEventRegistration {
    fun update() = isCheckedIf(condition(property1.value, property2.value))
    update()
    val registration = property1.onChange { update() }
    val otherRegistration = property2.onChange { update() }
    return object : CSEventRegistration {
        override var isActive = true
        override fun cancel() {
            isActive = false
            registration.cancel()
            otherRegistration.cancel()
        }
    }
}

fun CompoundButton.setOn() {
    isChecked = true
}

fun CompoundButton.setOff() {
    isChecked = false
}