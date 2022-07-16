package renetik.android.ui.extensions.widget

import android.content.res.ColorStateList
import android.widget.CompoundButton
import androidx.annotation.ColorInt
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.CSMultiRegistration
import renetik.android.event.property.CSProperty
import renetik.android.core.lang.variable.isTrue
import renetik.android.event.registration.paused

fun CompoundButton.onCheck(function: (CompoundButton) -> Unit) = apply {
    setOnCheckedChangeListener { buttonView, _ -> function(buttonView) }
}

fun CompoundButton.buttonTint(@ColorInt value: Int?) = apply {
    buttonTintList = if (value != null) ColorStateList.valueOf(value) else null
}

fun CompoundButton.checked(condition: Boolean = true) = apply {
    isChecked = condition
}

fun CompoundButton.isCheckedIf(condition: Boolean) = apply {
    isChecked = condition
}

fun CompoundButton.isCheckedIfNot(condition: Boolean) = apply {
    isChecked = !condition
}

fun CompoundButton.isCheckedIfNot(property: CSProperty<Boolean>): CSRegistration {
    val onChangeRegistration = property.onChange(this::isCheckedIfNot)
    onCheck { onChangeRegistration.paused { property.value(!isChecked) } }
    isCheckedIfNot(property.isTrue)
    return onChangeRegistration
}

fun CompoundButton.isCheckedIf(property: CSProperty<Boolean>): CSRegistration {
    val onChangeRegistration = property.onChange(this::isCheckedIf)
    isCheckedIf(property.isTrue)
    onCheck { onChangeRegistration.paused { property.value(isChecked) } }
    return onChangeRegistration
}

fun <T> CompoundButton.isCheckedIf(property1: CSProperty<T>, property2: CSProperty<*>,
                                   condition: (T) -> Boolean) =
    isCheckedIf(property1, property2) { first, _ -> condition(first) }

fun <T, V> CompoundButton.isCheckedIf(property1: CSProperty<T>, property2: CSProperty<V>,
                                      condition: (T, V) -> Boolean): CSRegistration {
    fun update() = isCheckedIf(condition(property1.value, property2.value))
    update()
    return CSMultiRegistration(
        property1.onChange { update() },
        property2.onChange { update() })
}

fun CompoundButton.setOn() {
    isChecked = true
}

fun CompoundButton.setOff() {
    isChecked = false
}