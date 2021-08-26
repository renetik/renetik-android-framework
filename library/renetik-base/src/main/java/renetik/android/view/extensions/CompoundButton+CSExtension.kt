package renetik.android.view.extensions

import android.content.res.ColorStateList
import android.widget.CompoundButton
import androidx.annotation.ColorInt
import renetik.android.framework.event.CSEvent.CSEventRegistration
import renetik.android.framework.event.pause
import renetik.android.framework.event.property.CSEventProperty
import renetik.android.framework.lang.isTrue

fun CompoundButton.onChecked(function: (CompoundButton) -> Unit) = apply {
    setOnCheckedChangeListener { buttonView, _ -> function(buttonView) }
}

fun CompoundButton.buttonTint(@ColorInt value: Int?) = apply {
    buttonTintList = if (value != null) ColorStateList.valueOf(value) else null
}

fun CompoundButton.isCheckedIf(property: CSEventProperty<Boolean>): CSEventRegistration {
    val onChangeRegistration = property.onChange {
        isChecked = it
    }
    onChecked { onChangeRegistration.pause().use { property.value(isChecked) } }
    isChecked = property.isTrue
    return onChangeRegistration
}

fun CompoundButton.setOn() {
    isChecked = true
}

fun CompoundButton.setOff() {
    isChecked = false
}