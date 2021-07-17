package renetik.android.view.extensions

import android.content.res.ColorStateList
import android.widget.CompoundButton
import androidx.annotation.ColorInt
import renetik.android.java.event.*

fun CompoundButton.onChecked(function: (CompoundButton) -> Unit) = apply {
    setOnCheckedChangeListener { buttonView, _ -> function(buttonView) }
}

fun CompoundButton.buttonTint(@ColorInt value: Int?) = apply {
    buttonTintList = if (value != null) ColorStateList.valueOf(value) else null
}

fun CompoundButton.valueProperty(parent: CSEventOwner, property: CSEventProperty<Boolean>) = apply {
    val onChangeRegistration = parent.register(property.onChange {
        isChecked = it
    })
    onChecked {
        onChangeRegistration.pause()
        property.value(it.isChecked)
        onChangeRegistration.resume()
    }
    isChecked = property.isTrue
}