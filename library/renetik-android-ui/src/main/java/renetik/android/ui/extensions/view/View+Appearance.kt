package renetik.android.ui.extensions.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color.argb
import android.graphics.Color.blue
import android.graphics.Color.green
import android.graphics.Color.red
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import renetik.android.core.extensions.content.CSColorInt
import renetik.android.core.extensions.content.dpToPixel
import renetik.android.event.registration.CSHasChangeValue
import renetik.android.event.registration.CSHasChangeValue.Companion.action
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

fun View.backgroundAlpha(alpha: Double) {
    (background as? ColorDrawable)?.color?.let {
        val color = argb((256 * alpha).toInt(), red(it), green(it), blue(it))
        background = ColorDrawable(color)
    } ?: run {
        background.alpha = (255 * alpha).toInt();
    }
}

fun <T : View> T.foregroundTint(@ColorInt value: Int) = apply {
    foregroundTintList = ColorStateList.valueOf(value)
}

val <T : View> T.foregroundTint: Int get() = foregroundTintList!!.defaultColor

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

fun View.enabledByAlphaIf(condition: Boolean, disable: Boolean = true) =
    disabledByAlpha(!condition, disable)

fun View.disabledByAlpha(condition: Boolean = true, disable: Boolean = true) {
    if (disable) disabledIf(condition)
    alphaToDisabled(condition)
}

fun View.alphaToDisabled(value: Boolean = true) {
    alpha = if (value) context.disabledAlpha else 1F
}

val Context.disabledAlpha get() = resources.getString(R.string.cs_disabled_alpha).toFloat()

inline fun <T> View.enabledByAlphaIf(
    property: CSHasChangeValue<T>, crossinline condition: (T) -> Boolean,
): CSRegistration = property.action { enabledByAlphaIf(condition(it)) }

fun View.enabledByAlphaIf(property: CSHasChangeValue<Boolean>) =
    enabledByAlphaIf(property) { it }

fun View.disabledByAlphaIf(
    property: CSHasChangeValue<Boolean>, disable: Boolean = true,
): CSRegistration = disabledByAlphaIf(property, disable) { it }

@Deprecated("Use or")
fun View.disabledByAlphaIf(
    property1: CSHasChangeValue<Boolean>, property2: CSHasChangeValue<Boolean>,
) = disabledByAlphaIf(property1, property2) { one, two -> one or two }

fun View.disabledByAlphaIfNot(
    property: CSHasChangeValue<Boolean>, disable: Boolean = true,
) = disabledByAlphaIf(property, disable) { !it }

inline fun <T> View.disabledByAlphaIf(
    property: CSHasChangeValue<T>, disable: Boolean = true,
    crossinline condition: (T) -> Boolean,
): CSRegistration = property.action { disabledByAlpha(condition(it), disable) }

inline fun <T> View.enabledByAlphaIf(
    property1: CSHasChangeValue<T>, property2: CSHasChangeValue<*>,
    crossinline condition: (T) -> Boolean,
) = enabledByAlphaIf(property1, property2) { first, _ -> condition(first) }

fun <T, V> View.enabledByAlphaIf(
    property1: CSHasChangeValue<T>, property2: CSHasChangeValue<V>, condition: (T, V) -> Boolean,
): CSRegistration {
    fun update() = enabledByAlphaIf(condition(property1.value, property2.value))
    update()
    return CSRegistration(property1.onChange { update() }, property2.onChange { update() })
}

fun <T, V> View.disabledByAlphaIf(
    property1: CSHasChangeValue<T>, property2: CSHasChangeValue<V>,
    disable: Boolean = true, condition: (T, V) -> Boolean,
): CSRegistration {
    fun update() = disabledByAlpha(condition(property1.value, property2.value), disable)
    update()
    return CSRegistration(property1.onChange { update() }, property2.onChange { update() })
}

fun <T, V, X> View.disabledByAlphaIf(
    property1: CSHasChangeValue<T>, property2: CSHasChangeValue<V>,
    property3: CSHasChangeValue<X>, disable: Boolean = true,
    condition: (T, V, X) -> Boolean,
): CSRegistration {
    fun update() = disabledByAlpha(
        condition(property1.value, property2.value, property3.value), disable
    )
    update()
    return CSRegistration(property1.onChange { update() },
        property2.onChange { update() }, property3.onChange { update() })
}
