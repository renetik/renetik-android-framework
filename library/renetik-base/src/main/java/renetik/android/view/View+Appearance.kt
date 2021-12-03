package renetik.android.view.extensions

import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import renetik.android.content.CSColorInt
import renetik.android.content.attributeFloat
import renetik.android.content.dpToPixel
import renetik.android.framework.event.CSEventRegistration
import renetik.android.framework.event.CSMultiEventRegistration
import renetik.android.framework.event.property.CSEventProperty
import renetik.android.view.disabledIf


fun <T : View> T.background(@DrawableRes value: Int) = apply {
    setBackgroundResource(value)
}

fun <T : View> T.backgroundColor(@ColorInt value: Int) = apply {
    setBackgroundColor(value)
}

fun <T : View> T.backgroundColor(value: CSColorInt) = apply {
    setBackgroundColor(value.color)
}

fun <T : View> T.backgroundTint(@ColorInt value: Int) = apply {
    backgroundTintList = ColorStateList.valueOf(value)
}

@RequiresApi(Build.VERSION_CODES.M)
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

fun View.disabledByAlpha(condition: Boolean = true) {
    disabledIf(condition)
    alpha = if (condition) context.attributeFloat(android.R.attr.disabledAlpha) else 1F
}

fun View.alphaToDisabled() {
    alpha = context.attributeFloat(android.R.attr.disabledAlpha)
}

fun <T> View.enabledByAlphaIf(property: CSEventProperty<T>,
                              condition: (T) -> Boolean): CSEventRegistration {
    enabledByAlphaIf(condition(property.value))
    return property.onChange { enabledByAlphaIf(condition(property.value)) }
}

fun View.enabledByAlphaIf(property: CSEventProperty<Boolean>) =
    enabledByAlphaIf(property) { it }

fun View.disabledByAlphaIf(property: CSEventProperty<Boolean>) =
    disabledByAlphaIf(property) { it }

fun <T> View.disabledByAlphaIf(property: CSEventProperty<T>,
                               condition: (T) -> Boolean): CSEventRegistration {
    disabledByAlpha(condition(property.value))
    return property.onChange { disabledByAlpha(condition(property.value)) }
}

fun <T> View.enabledByAlphaIf(property1: CSEventProperty<T>, property2: CSEventProperty<*>,
                              condition: (T) -> Boolean) =
    enabledByAlphaIf(property1, property2) { first, _ -> condition(first) }

fun <T, V> View.enabledByAlphaIf(property1: CSEventProperty<T>, property2: CSEventProperty<V>,
                                 condition: (T, V) -> Boolean): CSEventRegistration {
    fun update() = enabledByAlphaIf(condition(property1.value, property2.value))
    update()
    return CSMultiEventRegistration(
        property1.onChange { update() },
        property2.onChange { update() })
}

fun <T, V> View.disabledByAlphaIf(property1: CSEventProperty<T>, property2: CSEventProperty<V>,
                                  condition: (T, V) -> Boolean): CSEventRegistration {
    fun update() = disabledByAlpha(condition(property1.value, property2.value))
    update()
    return CSMultiEventRegistration(
        property1.onChange { update() },
        property2.onChange { update() })
}
