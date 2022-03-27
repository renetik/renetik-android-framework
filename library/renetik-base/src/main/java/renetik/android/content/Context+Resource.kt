package renetik.android.content

import android.content.Context
import android.content.res.Configuration.*
import android.content.res.Resources.NotFoundException
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.DisplayMetrics
import android.util.DisplayMetrics.DENSITY_DEFAULT
import android.util.TypedValue
import android.util.TypedValue.*
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat.getColor
import renetik.android.R
import renetik.android.framework.common.*
import renetik.kotlin.asString
import renetik.kotlin.collections.list
import renetik.kotlin.isAny
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.InputStream

class CSColorInt(@ColorInt val color: Int)

fun Context.color(@ColorRes color: Int) = CSColorInt(getColor(this, color))
fun Context.colorInt(@ColorInt color: Int) = CSColorInt(color)

fun Context.resourceBytes(id: Int) = catchAllWarn {
    val stream = resources.openRawResource(id)
    val outputStream = ByteArrayOutputStream()
    val buffer = ByteArray(4 * 1024)
    tryAndFinally({
        var read: Int
        do {
            read = stream.read(buffer, 0, buffer.size)
            if (read == -1) break
            outputStream.write(buffer, 0, read)
        } while (true)
        outputStream.toByteArray()
    }) { stream.close() }
}

fun Context.openInputStream(uri: Uri) = catchErrorReturnNull<FileNotFoundException, InputStream> {
    return contentResolver.openInputStream(uri)
}

fun Context.resourceDimension(id: Int) = resources.getDimension(id).toInt()

fun Context.resourceStrings(id: Int) = catchWarnReturnNull<List<String>, NotFoundException> {
    list(*resources.getStringArray(id))
}

fun Context.resourceInts(id: Int) = catchError<NotFoundException> {
    list(resources.getIntArray(id).asList())
}

val Context.displayMetrics get():DisplayMetrics = resources.displayMetrics

fun Context.toDpF(pixel: Float) = pixel / (displayMetrics.densityDpi.toFloat() / DENSITY_DEFAULT)
fun Context.toDpF(pixel: Int) = toDpF(pixel.toFloat())
fun Context.toDp(pixel: Int) = toDpF(pixel).toInt()

fun Context.dpToPixelF(dp: Float) = applyDimension(COMPLEX_UNIT_DIP, dp, displayMetrics)
fun Context.dpToPixelF(dp: Int) = dpToPixelF(dp.toFloat())
fun Context.dpToPixel(dp: Float) = dpToPixelF(dp).toInt()
fun Context.dpToPixel(dp: Int) = dpToPixelF(dp.toFloat()).toInt()

fun Context.spToPixelF(sp: Float) = applyDimension(COMPLEX_UNIT_SP, sp, displayMetrics)
fun Context.spToPixelF(sp: Int) = dpToPixelF(sp.toFloat())
fun Context.spToPixel(sp: Float) = dpToPixelF(sp).toInt()
fun Context.spToPixel(sp: Int) = dpToPixelF(sp.toFloat()).toInt()

private fun Context.attributeValue(@AttrRes attribute: Int) =
    TypedValue().apply { theme.resolveAttribute(attribute, this, true) }

@ColorInt
fun Context.attributeColor(@AttrRes attribute: Int) = attributeValue(attribute).data.apply {
    if (this == 0) throw NotFoundException()
}

fun Context.attributeDimensionPixel(@AttrRes attribute: Int): Int {
    val attributes = obtainStyledAttributes(intArrayOf(attribute))
    val dimension = attributes.getDimensionPixelSize(0, 0)
    attributes.recycle()
    return dimension
}

fun Context.attributeDimension(@AttrRes attribute: Int): Float {
    val attributes = obtainStyledAttributes(intArrayOf(attribute))
    val dimension = attributes.getDimension(0, 0f)
    attributes.recycle()
    return dimension
}

fun Context.attributeFloat(@AttrRes attribute: Int): Float {
    val attributes = obtainStyledAttributes(intArrayOf(attribute))
    val float = attributes.getFloat(0, 0F)
    attributes.recycle()
    return float
}

fun Context.attributeString(@AttrRes attribute: Int) = attributeValue(attribute).string.asString

fun Context.attributeString2(@AttrRes attribute: Int) = attributeString(intArrayOf(attribute), 0)

fun Context.attributeString(styleable: IntArray, styleableAttribute: Int): String {
    val attributes = obtainStyledAttributes(styleable)
    val string = attributes.getString(styleableAttribute)
    attributes.recycle()
    return string.asString
}

fun Context.attributeResourceId(@AttrRes attribute: Int) =
    attributeValue(attribute).resourceId.apply {
        if (this == 0) throw NotFoundException()
    }

fun Context.assetsReadText(path: String) = assets.open(path).bufferedReader().use { it.readText() }

val Context.isPortrait get() = resources.configuration.orientation == ORIENTATION_PORTRAIT
val Context.isLandscape get() = !isPortrait
val Context.isTablet get() = resources.getBoolean(R.bool.cs_is_tablet)
val Context.isPhone get() = !isTablet

fun Context.drawable(@DrawableRes resource: Int) =
    AppCompatResources.getDrawable(this, resource)!!.apply {
        setBounds(0, 0, intrinsicWidth, intrinsicHeight)
    }

fun clearDrawable(drawable: Drawable) = clearDrawable(drawable.bounds)
fun clearDrawable(bounds: Rect) = ColorDrawable().apply { this.bounds = bounds }

val Context.isDarkMode
    get() = if (getDefaultNightMode()
            .isAny(MODE_NIGHT_FOLLOW_SYSTEM, MODE_NIGHT_UNSPECIFIED)) isSystemDarkMode
    else getDefaultNightMode() == MODE_NIGHT_YES

val Context.isSystemDarkMode
    get() = resources.configuration.uiMode and UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES
