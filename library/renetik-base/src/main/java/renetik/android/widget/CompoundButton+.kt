package renetik.android.widget

import android.content.res.ColorStateList
import android.widget.CompoundButton
import androidx.annotation.ColorInt
import renetik.android.event.CSRegistration
import renetik.android.event.CSMultiEventRegistration
import renetik.android.event.pause
import renetik.android.framework.event.property.CSEventProperty
import renetik.android.core.lang.property.isTrue

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

fun CompoundButton.isCheckedIfNot(property: CSEventProperty<Boolean>): CSRegistration {
    val onChangeRegistration = property.onChange(this::isCheckedIfNot)
    onCheck { onChangeRegistration.pause().use { property.value(!isChecked) } }
    isCheckedIfNot(property.isTrue)
    return onChangeRegistration
}

fun CompoundButton.isCheckedIf(property: CSEventProperty<Boolean>): CSRegistration {
    val onChangeRegistration = property.onChange(this::isCheckedIf)
    isCheckedIf(property.isTrue)
    onCheck { onChangeRegistration.pause().use { property.value(isChecked) } }
    return onChangeRegistration
}

fun <T> CompoundButton.isCheckedIf(property1: CSEventProperty<T>, property2: CSEventProperty<*>,
                                   condition: (T) -> Boolean) =
    isCheckedIf(property1, property2) { first, _ -> condition(first) }

fun <T, V> CompoundButton.isCheckedIf(property1: CSEventProperty<T>, property2: CSEventProperty<V>,
                                      condition: (T, V) -> Boolean): CSRegistration {
    fun update() = isCheckedIf(condition(property1.value, property2.value))
    update()
    return CSMultiEventRegistration(
        property1.onChange { update() },
        property2.onChange { update() })
}

fun CompoundButton.setOn() {
    isChecked = true
}

fun CompoundButton.setOff() {
    isChecked = false
}