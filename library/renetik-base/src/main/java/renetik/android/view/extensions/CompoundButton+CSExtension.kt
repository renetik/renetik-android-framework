package renetik.android.view.extensions

import android.content.res.ColorStateList
import android.widget.CompoundButton
import androidx.annotation.ColorInt
import renetik.android.framework.event.CSVisibleEventOwner
import renetik.android.framework.event.pause
import renetik.android.framework.event.property.CSEventProperty
import renetik.android.framework.lang.isTrue

fun CompoundButton.onChecked(function: (CompoundButton) -> Unit) = apply {
    setOnCheckedChangeListener { buttonView, _ -> function(buttonView) }
}

fun CompoundButton.buttonTint(@ColorInt value: Int?) = apply {
    buttonTintList = if (value != null) ColorStateList.valueOf(value) else null
}

fun CompoundButton.isCheckedIf(parent: CSVisibleEventOwner, property: CSEventProperty<Boolean>) =
    apply {
        val onChangeRegistration = parent.whileVisible(property.onChange {
            isChecked = it
        })
        onChecked { onChangeRegistration.pause().use { property.value(isChecked) } }
        isChecked = property.isTrue
    }

fun CompoundButton.isCheckedIf(property: CSEventProperty<Boolean>) = apply {
    onChecked { property.value(it.isChecked) }
    isChecked = property.isTrue
}