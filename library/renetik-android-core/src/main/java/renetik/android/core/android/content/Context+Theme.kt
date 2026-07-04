package renetik.android.core.android.content

import android.R
import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.content.res.Resources.NotFoundException
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.ContextThemeWrapper
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_UNSPECIFIED
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate.getDefaultNightMode
import androidx.core.content.ContextCompat
import androidx.core.content.res.getColorOrThrow
import androidx.core.content.res.use
import renetik.android.core.kotlin.asString
import renetik.android.core.kotlin.equalsAny

private fun Context.attributeValue(@AttrRes attribute: Int) =
    TypedValue().apply { theme.resolveAttribute(attribute, this, true) }

@ColorInt
fun Context.themeAttributeColor(theme: Int, attribute: Int): Int =
    ContextThemeWrapper(this, theme).attributeColor(attribute)

@ColorInt
fun Context.resourceColor(@ColorRes resource: Int): Int = getColor(resource)

@ColorInt
fun Context.attributeColor(@AttrRes attribute: Int): Int =
    attributeValue(attribute).data.apply {
        if (this == 0) throw NotFoundException()
    }

fun Context.attributeDrawable(@AttrRes attribute: Int): Drawable? =
    attributeValue(attribute).let { ContextCompat.getDrawable(this, it.resourceId) }

@ColorInt
fun Context.attributeColorOrNull(@AttrRes attribute: Int): Int? =
    attributeValue(attribute).data.takeIf { it != 0 }

fun Context.attributeDimensionPixel(@AttrRes attribute: Int, default: Int = 0): Int {
    val attributes = obtainStyledAttributes(intArrayOf(attribute))
    val dimension = attributes.getDimensionPixelSize(0, default)
    attributes.recycle()
    return dimension
}

fun Context.attributeDimension(@AttrRes attribute: Int, default: Float = 0f): Float {
    val attributes = obtainStyledAttributes(intArrayOf(attribute))
    val dimension = attributes.getDimension(0, default)
    attributes.recycle()
    return dimension
}

fun Context.attributeInt(@AttrRes attribute: Int, default: Int = 0): Int {
    val attributes = obtainStyledAttributes(intArrayOf(attribute))
    val value = attributes.getInt(0, default)
    attributes.recycle()
    return value
}

fun Context.attributeFloat(@AttrRes attribute: Int, default: Float = 0f): Float {
    val attributes = obtainStyledAttributes(intArrayOf(attribute))
    val float = attributes.getFloat(0, default)
    attributes.recycle()
    return float
}

fun Context.attributeString(@AttrRes attribute: Int) =
    attributeValue(attribute).string.asString

fun Context.attributeString2(@AttrRes attribute: Int) =
    attributeString(intArrayOf(attribute), 0)

fun Context.attributeString(styleable: IntArray, styleableAttribute: Int): String {
    val attributes = obtainStyledAttributes(styleable)
    val string = attributes.getString(styleableAttribute)
    attributes.recycle()
    return string.asString
}

fun Context.attributeResourceId(@AttrRes attribute: Int) = attributeValue(attribute)
    .resourceId.apply { if (this == 0) throw NotFoundException() }

val Context.isDarkMode: Boolean
    get() = getDefaultNightMode().let {
        if (it.equalsAny(
                MODE_NIGHT_FOLLOW_SYSTEM, MODE_NIGHT_UNSPECIFIED
            )
        ) isSystemDarkMode
        else it == MODE_NIGHT_YES
    }

val Context.isSystemDarkMode
    get() = resources.configuration.uiMode and UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES

@ColorInt
fun Context.colorFromStyle(@StyleRes style: Int): Int? =
    obtainStyledAttributes(style, intArrayOf(R.attr.textColor)).use {
        runCatching { it.getColorOrThrow(0) }.getOrNull()
    }
