package renetik.android.view.extensions

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.FontRes
import androidx.core.content.res.ResourcesCompat

fun TextView.textColor(@ColorInt value: Int) = apply {
    setTextColor(ColorStateList.valueOf(value))
}

fun TextView.typeface(@FontRes font: Int) = apply {
    typeface = ResourcesCompat.getFont(context, font)
}

fun TextView.setBottomDrawable(drawable: Drawable?) =
    setCompoundDrawables(null, null, null, drawable)