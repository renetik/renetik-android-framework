package renetik.android.widget

import android.annotation.SuppressLint
import android.content.ContextWrapper
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Build
import android.widget.TextView
import androidx.annotation.*
import androidx.core.content.res.ResourcesCompat
import renetik.android.content.CSColorInt
import renetik.android.content.attributeColor
import renetik.android.content.color
import renetik.android.content.drawable
import renetik.android.framework.event.CSRegistration
import renetik.android.framework.event.property.CSEventProperty
import renetik.android.primitives.at

fun TextView.textColor(value: CSColorInt) = apply {
    setTextColor(value.color)
}

fun TextView.textColorInt(@ColorInt color: Int) = apply {
    setTextColor(color)
}

fun TextView.textColor(@ColorRes value: Int) = apply {
    textColor(context.color(value))
}

fun TextView.textColorAttr(@ColorRes value: Int) = apply {
    setTextColor(context.attributeColor(value))
}

@RequiresApi(Build.VERSION_CODES.M)
fun TextView.drawableTintAttr(value: Int) = apply {
    setDrawableTint(context.attributeColor(value))
}

fun TextView.typeface(@FontRes font: Int) = apply {
    typeface = ResourcesCompat.getFont(context, font)
}

fun <T : TextView> T.startDrawable(drawable: Drawable?) = apply {
    setCompoundDrawables(drawable, compoundDrawables.at(1),
        compoundDrawables.at(2), compoundDrawables.at(3))
}

fun <T : TextView> T.startDrawable(@DrawableRes drawable: Int?) =
    startDrawable(drawable?.let { context.drawable(it) })

fun <T : TextView> T.startDrawable(property: CSEventProperty<Boolean>,
                                   function: (Boolean) -> Int): CSRegistration {
    fun update(value: Boolean) = startDrawable(function(value))
    update(property.value)
    return property.onChange { update(it) }
}

fun TextView.topDrawable(@DrawableRes drawable: Int?) =
    setCompoundDrawables(compoundDrawables.at(0), drawable?.let { context.drawable(it) },
        compoundDrawables.at(2), compoundDrawables.at(3))

fun TextView.endDrawable(drawable: Drawable?) =
    setCompoundDrawables(compoundDrawables.at(0), compoundDrawables.at(1),
        drawable, compoundDrawables.at(3))

fun TextView.endDrawable(@DrawableRes drawable: Int?) =
    endDrawable(drawable?.let { context.drawable(it) })

fun TextView.bottomDrawable(@DrawableRes drawable: Int?) =
    setCompoundDrawables(compoundDrawables.at(0), compoundDrawables.at(1),
        compoundDrawables.at(2), drawable?.let { context.drawable(it) })

@SuppressLint("UseCompatTextViewDrawableApis")
@RequiresApi(Build.VERSION_CODES.M)
fun TextView.setDrawableTint(context: ContextWrapper, @ColorRes iconColor: Int) {
    compoundDrawableTintList = context.getColorStateList(iconColor)
}

@SuppressLint("UseCompatTextViewDrawableApis")
@RequiresApi(Build.VERSION_CODES.M)
fun TextView.setDrawableTint(@ColorInt colorInt: Int) {
    compoundDrawableTintList = ColorStateList.valueOf(colorInt)
}