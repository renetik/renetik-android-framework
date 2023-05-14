package renetik.android.ui.extensions.widget

import android.annotation.SuppressLint
import android.content.ContextWrapper
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Build
import android.widget.TextView
import androidx.annotation.*
import androidx.core.content.res.ResourcesCompat
import renetik.android.core.extensions.content.CSColorInt
import renetik.android.core.extensions.content.attributeColor
import renetik.android.core.extensions.content.color
import renetik.android.core.extensions.content.drawable
import renetik.android.core.kotlin.primitives.at
import renetik.android.event.property.CSProperty
import renetik.android.event.registration.CSRegistration

fun TextView.textColor(value: CSColorInt) = apply {
    setTextColor(value.color)
}

fun TextView.textColorInt(@ColorInt color: Int) = apply {
    setTextColor(color)
}

fun TextView.textColor(@ColorRes value: Int) = apply {
    textColor(context.color(value))
}

fun TextView.textColorAttr(@AttrRes attribute: Int) = apply {
    setTextColor(context.attributeColor(attribute))
}

@RequiresApi(Build.VERSION_CODES.M)
fun TextView.drawableTintAttr(@AttrRes value: Int) = apply {
    setDrawableTint(context.attributeColor(value))
}

fun TextView.typeface(@FontRes font: Int) = apply {
    typeface = ResourcesCompat.getFont(context, font)
}

//all compoundDrawables needs to be set programmatically
fun <T : TextView> T.startDrawable(drawable: Drawable?) = apply {
    setCompoundDrawables(
        drawable, compoundDrawables.at(1),
        compoundDrawables.at(2), compoundDrawables.at(3)
    )
}

//all compoundDrawables needs to be set programmatically
fun <T : TextView> T.startDrawable(@DrawableRes drawable: Int?) =
    startDrawable(drawable?.let { context.drawable(it) })

//all compoundDrawables needs to be set programmatically
fun <T : TextView> T.startDrawable(
    property: CSProperty<Boolean>,
    function: (Boolean) -> Int
): CSRegistration {
    fun update(value: Boolean) = startDrawable(function(value))
    update(property.value)
    return property.onChange { update(it) }
}

//all compoundDrawables needs to be set programmatically
fun TextView.topDrawable(@DrawableRes drawable: Int?) =
    setCompoundDrawables(
        compoundDrawables.at(0), drawable?.let { context.drawable(it) },
        compoundDrawables.at(2), compoundDrawables.at(3)
    )

//all compoundDrawables needs to be set programmatically
fun TextView.endDrawable(drawable: Drawable?) =
    setCompoundDrawables(
        compoundDrawables.at(0), compoundDrawables.at(1),
        drawable, compoundDrawables.at(3)
    )

//all compoundDrawables needs to be set programmatically
fun TextView.endDrawable(@DrawableRes drawable: Int?) =
    endDrawable(drawable?.let { context.drawable(it) })

//all compoundDrawables needs to be set programmatically
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