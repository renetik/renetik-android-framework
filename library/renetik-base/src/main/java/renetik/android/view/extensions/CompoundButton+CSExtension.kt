package renetik.android.view.extensions

import android.content.res.ColorStateList
import android.widget.CompoundButton
import androidx.annotation.ColorInt
import renetik.android.java.event.CSEventProperty
import renetik.android.java.event.isTrue

fun CompoundButton.onChecked(function: (CompoundButton) -> Unit) = apply {
    setOnCheckedChangeListener { buttonView, _ -> function(buttonView) }
}

fun CompoundButton.buttonTint(@ColorInt value: Int?) = apply {
    buttonTintList = if (value != null) ColorStateList.valueOf(value) else null
}

fun CompoundButton.valueProperty(property: CSEventProperty<Boolean>) = apply {
    onChecked { property.value(it.isChecked) }.isChecked = property.isTrue
}