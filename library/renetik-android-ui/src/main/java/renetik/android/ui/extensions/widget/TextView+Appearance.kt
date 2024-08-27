package renetik.android.ui.extensions.widget

import android.annotation.SuppressLint
import android.content.ContextWrapper
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.core.content.res.ResourcesCompat
import renetik.android.core.common.CSColor
import renetik.android.core.common.CSColor.Companion.colorRes
import renetik.android.core.extensions.content.attributeColor
import renetik.android.core.extensions.content.drawable
import renetik.android.core.kotlin.primitives.at
import renetik.android.event.property.CSProperty
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.action

fun TextView.textColor(value: CSColor) = apply {
    setTextColor(value.color)
}

fun TextView.textColorInt(@ColorInt color: Int) = apply {
    setTextColor(color)
}

fun TextView.textColor(@ColorRes value: Int) = apply {
    textColor(context.colorRes(value))
}

fun TextView.textColorAttr(@AttrRes attribute: Int) = apply {
    setTextColor(context.attributeColor(attribute))
}

fun TextView.drawableTintAttr(@AttrRes value: Int) = apply {
    setDrawableTint(context.attributeColor(value))
}

fun TextView.typeface(@FontRes font: Int) = apply {
    typeface = ResourcesCompat.getFont(context, font)
}

fun <T : TextView> T.drawable(
    @DrawableRes start: Int? = null, @DrawableRes top: Int? = null,
    @DrawableRes end: Int? = null, @DrawableRes bottom: Int? = null
) = apply {
    setCompoundDrawables(
        start?.let { context.drawable(it) } ?: compoundDrawables.at(0),
        top?.let { context.drawable(it) } ?: compoundDrawables.at(1),
        end?.let { context.drawable(it) } ?: compoundDrawables.at(2),
        bottom?.let { context.drawable(it) } ?: compoundDrawables.at(3)
    )
}

fun <T : TextView> T.drawableEnd(@DrawableRes end: Int?) = apply {
    setCompoundDrawables(
        compoundDrawables.at(0),
        compoundDrawables.at(1),
        end?.let { context.drawable(it) },
        compoundDrawables.at(3)
    )
}

fun <T : TextView> T.drawable(
    start: Drawable? = null, top: Drawable? = null,
    end: Drawable? = null, bottom: Drawable? = null
) = apply {
    setCompoundDrawables(
        start ?: compoundDrawables.at(0), top ?: compoundDrawables.at(1),
        end ?: compoundDrawables.at(2), bottom ?: compoundDrawables.at(3)
    )
}

fun <TextViewType : TextView, T> TextViewType.drawableTop(
    property: CSProperty<T>, function: (T) -> Int
): CSRegistration = property.action { drawable(top = function(it)) }

fun <TextViewType : TextView, T> TextViewType.drawableStart(
    property: CSProperty<T>, function: (T) -> Int
): CSRegistration = property.action { drawable(start = function(it)) }


//@Deprecated("use drawable", ReplaceWith("drawable"))
//fun <T : TextView> T.startDrawable(drawable: Drawable?) = drawable(start = drawable)

//@Deprecated("use drawable", ReplaceWith("drawable"))
//fun <T : TextView> T.startDrawable(@DrawableRes drawable: Int?) = drawable(start = drawable)

//@Deprecated("use drawable", ReplaceWith("drawable"))
//fun TextView.topDrawable(@DrawableRes drawable: Int?) = drawable(top = drawable)

//@Deprecated("use drawable", ReplaceWith("drawable"))
//fun TextView.endDrawable(drawable: Drawable?) = drawable(end = drawable)

//@Deprecated("use drawable", ReplaceWith("drawable"))
//fun TextView.endDrawable(@DrawableRes drawable: Int?) = drawable(end = drawable)

//@Deprecated("use drawable", ReplaceWith("drawable"))
//fun TextView.bottomDrawable(@DrawableRes drawable: Int?) = drawable(bottom = drawable)

@SuppressLint("UseCompatTextViewDrawableApis")
fun TextView.setDrawableTint(context: ContextWrapper, @ColorRes iconColor: Int) {
    compoundDrawableTintList = context.getColorStateList(iconColor)
}

@SuppressLint("UseCompatTextViewDrawableApis")
fun TextView.setDrawableTint(@ColorInt colorInt: Int) {
    compoundDrawableTintList = ColorStateList.valueOf(colorInt)
}