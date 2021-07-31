package renetik.android.view.extensions

import android.annotation.SuppressLint
import android.content.ContextWrapper
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Build
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.FontRes
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import renetik.android.content.CSColorInt
import renetik.android.content.color

fun TextView.textColor(value: CSColorInt) = apply {
    setTextColor(ColorStateList.valueOf(value.color))
}

fun TextView.textColor(@ColorRes value: Int) = apply {
    textColor(context.color(value))
}

fun TextView.typeface(@FontRes font: Int) = apply {
    typeface = ResourcesCompat.getFont(context, font)
}

fun TextView.bottomDrawable(drawable: Drawable?) =
    setCompoundDrawables(null, null, null, drawable)

fun TextView.setEndDrawable(drawable: Drawable?) =
    setCompoundDrawables(null, null, drawable, null)

fun TextView.setStartDrawable(drawable: Drawable?) =
    setCompoundDrawables(drawable, null, null, null)

@SuppressLint("UseCompatTextViewDrawableApis")
@RequiresApi(Build.VERSION_CODES.M)
fun TextView.setDrawableTint(context: ContextWrapper, @ColorRes iconColor: Int) {
    compoundDrawableTintList = context.getColorStateList(iconColor)
}