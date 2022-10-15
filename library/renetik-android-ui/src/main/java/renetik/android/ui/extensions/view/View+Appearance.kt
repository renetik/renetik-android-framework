package renetik.android.ui.extensions.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import renetik.android.core.extensions.content.CSColorInt
import renetik.android.core.extensions.content.dpToPixel
import renetik.android.event.registration.CSHasChangeValue
import renetik.android.event.registration.CSRegistration
import renetik.android.ui.R

fun <T : View> T.background(@DrawableRes value: Int) = apply {
    setBackgroundResource(value)
}

fun <T : View> T.backgroundColor(@ColorInt value: Int) = apply {
    setBackgroundColor(value)
}

fun <T : View> T.background(value: CSColorInt) = apply {
    setBackgroundColor(value.color)
}

fun <T : View> T.backgroundTint(@ColorInt value: Int) = apply {
    backgroundTintList = ColorStateList.valueOf(value)
}

fun <T : View> T.foregroundTint(@ColorInt value: Int) = apply {
    foregroundTintList = ColorStateList.valueOf(value)
}

fun <T : View> T.backgroundRoundedWithColor(@ColorInt value: Int, radius: Float = 8f) {
    val shape = GradientDrawable()
    shape.cornerRadius = radius
    shape.color = ColorStateList.valueOf(value)
    background = shape
}

fun <T : View> T.paddingDp(left: Int, top: Int, right: Int, bottom: Int) = apply {
    setPadding(
        context.dpToPixel(left), context.dpToPixel(top),
        context.dpToPixel(right), context.dpToPixel(bottom)
    )
}

fun <T : View> T.paddingDp(horizontal: Int = -1, vertical: Int = -1) = apply {
    if (horizontal >= 0) paddingHorizontal(dp = horizontal)
    if (vertical >= 0) paddingVertical(dp = horizontal)
}

fun <T : View> T.padding(dp: Int = -1, px: Int = -1) = apply {
    val pixelValue = if (dp > -0) context.dpToPixel(dp) else if (px >= 0) px else 0
    paddingDp(pixelValue, pixelValue, pixelValue, pixelValue)
}

fun <T : View> T.paddingHorizontal(dp: Int = -1, px: Int = -1) = apply {
    val pixelValue = if (dp > -0) context.dpToPixel(dp) else if (px >= 0) px else 0
    setPadding(pixelValue, paddingTop, pixelValue, paddingBottom)
}

fun <T : View> T.paddingVertical(dp: Int = -1, px: Int = -1) = apply {
    val pixelValue = if (dp > -0) context.dpToPixel(dp) else if (px >= 0) px else 0
    setPadding(paddingLeft, pixelValue, paddingRight, pixelValue)
}

fun View.enabledByAlphaIf(condition: Boolean) = disabledByAlpha(!condition)

fun View.disabledByAlpha(condition: Boolean = true, disable: Boolean = true) {
    if (disable) disabledIf(condition)
    alphaToDisabled(condition)
}

fun View.alphaToDisabled(value: Boolean = true) {
    alpha = if (value) context.disabledAlpha else 1F
}

val Context.disabledAlpha
    get() = getResources().getString(R.string.cs_disabled_alpha).toFloat()

fun <T> View.enabledByAlphaIf(property: CSHasChangeValue<T>,
                              condition: (T) -> Boolean): CSRegistration {
    enabledByAlphaIf(condition(property.value))
    return property.onChange { enabledByAlphaIf(condition(property.value)) }
}

fun View.enabledByAlphaIf(property: CSHasChangeValue<Boolean>) =
    enabledByAlphaIf(property) { it }

fun View.disabledByAlphaIf(property: CSHasChangeValue<Boolean>, disable: Boolean = true) =
    disabledByAlphaIf(property, disable) { it }

@Deprecated("Use or")
fun View.disabledByAlphaIf(property1: CSHasChangeValue<Boolean>,
                           property2: CSHasChangeValue<Boolean>) =
    disabledByAlphaIf(property1, property2) { one, two -> one or two }

fun View.disabledByAlphaIfNot(property: CSHasChangeValue<Boolean>, disable: Boolean = true) =
    disabledByAlphaIf(property, disable) { !it }

fun <T> View.disabledByAlphaIf(property: CSHasChangeValue<T>, disable: Boolean = true,
                               condition: (T) -> Boolean): CSRegistration {
    disabledByAlpha(condition(property.value), disable)
    return property.onChange { disabledByAlpha(condition(property.value), disable) }
}

fun <T> View.enabledByAlphaIf(property1: CSHasChangeValue<T>, property2: CSHasChangeValue<*>,
                              condition: (T) -> Boolean) =
    enabledByAlphaIf(property1, property2) { first, _ -> condition(first) }

fun <T, V> View.enabledByAlphaIf(property1: CSHasChangeValue<T>, property2: CSHasChangeValue<V>,
                                 condition: (T, V) -> Boolean): CSRegistration {
    fun update() = enabledByAlphaIf(condition(property1.value, property2.value))
    update()
    return CSRegistration(property1.onChange { update() }, property2.onChange { update() })
}

fun <T, V> View.disabledByAlphaIf(property1: CSHasChangeValue<T>, property2: CSHasChangeValue<V>,
                                  condition: (T, V) -> Boolean): CSRegistration {
    fun update() = disabledByAlpha(condition(property1.value, property2.value))
    update()
    return CSRegistration(property1.onChange { update() }, property2.onChange { update() })
}
