package renetik.android.view.extensions

import android.content.res.ColorStateList
import android.widget.CompoundButton
import renetik.android.extensions.color

fun CompoundButton.onChecked(function: (CompoundButton) -> Unit) = apply {
    setOnCheckedChangeListener { buttonView, _ -> function(buttonView) }
}

fun CompoundButton.buttonTint(value: Int?) = apply {
    buttonTintList = if (value != null) ColorStateList.valueOf(context.color(value)) else null
}