package renetik.android.view.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Build
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.FontRes
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat

fun TextView.textColor(@ColorInt value: Int) = apply {
    setTextColor(ColorStateList.valueOf(value))
}

fun TextView.typeface(@FontRes font: Int) = apply {
    typeface = ResourcesCompat.getFont(context, font)
}

fun TextView.setBottomDrawable(drawable: Drawable?) =
    setCompoundDrawables(null, null, null, drawable)

fun TextView.setRightDrawable(drawable: Drawable?) =
    setCompoundDrawables(null, null, drawable, null)

@SuppressLint("UseCompatTextViewDrawableApis")
@RequiresApi(Build.VERSION_CODES.M)
fun TextView.setDrawableTint(context: ContextWrapper, @ColorRes iconColor: Int) {
    compoundDrawableTintList = context.getColorStateList(iconColor)
}