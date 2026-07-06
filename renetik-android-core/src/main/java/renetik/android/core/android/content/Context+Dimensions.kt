package renetik.android.core.android.content

import android.R
import android.content.Context
import android.util.DisplayMetrics
import android.util.DisplayMetrics.DENSITY_DEFAULT
import android.util.TypedValue.COMPLEX_UNIT_DIP
import android.util.TypedValue.COMPLEX_UNIT_SP
import android.util.TypedValue.applyDimension
import androidx.annotation.DimenRes

fun Context.dimensionInt(@DimenRes id: Int) = dimension(id).toInt()
fun Context.dimension(@DimenRes id: Int): Float = resources.getDimension(id)
fun Context.dimensionPx(@DimenRes id: Int): Int = resources.getDimensionPixelSize(id)
fun Context.dimensionPxFloat(@DimenRes id: Int): Float =
    resources.getDimensionPixelSize(id).toFloat()

val Context.displayMetrics get(): DisplayMetrics = resources.displayMetrics

private const val LOW_DPI_STATUS_BAR_HEIGHT = 19
private const val MEDIUM_DPI_STATUS_BAR_HEIGHT = 25
private const val HIGH_DPI_STATUS_BAR_HEIGHT = 38

val Context.statusBarHeight
    get() = when (displayMetrics.densityDpi) {
        DisplayMetrics.DENSITY_HIGH -> HIGH_DPI_STATUS_BAR_HEIGHT
        DisplayMetrics.DENSITY_MEDIUM -> MEDIUM_DPI_STATUS_BAR_HEIGHT
        DisplayMetrics.DENSITY_LOW -> LOW_DPI_STATUS_BAR_HEIGHT
        else -> MEDIUM_DPI_STATUS_BAR_HEIGHT
    }

fun Context.toDpF(pixel: Float) =
    pixel / (displayMetrics.densityDpi.toFloat() / DENSITY_DEFAULT)

fun Context.toDpF(pixel: Int) = toDpF(pixel.toFloat())
fun Context.toDp(pixel: Int) = toDpF(pixel).toInt()

fun Context.dpToPixelF(dp: Float): Float =
    applyDimension(COMPLEX_UNIT_DIP, dp, displayMetrics)

fun Context.dpToPixelF(dp: Int): Float = dpToPixelF(dp.toFloat())
fun Context.dpToPixel(dp: Float) = dpToPixelF(dp).toInt()
fun Context.dpToPixel(dp: Int) = dpToPixelF(dp.toFloat()).toInt()

fun Context.spToPixelF(sp: Float) = applyDimension(COMPLEX_UNIT_SP, sp, displayMetrics)
fun Context.spToPixelF(sp: Int) = dpToPixelF(sp.toFloat())
fun Context.spToPixel(sp: Float) = dpToPixelF(sp).toInt()
fun Context.spToPixel(sp: Int) = dpToPixelF(sp.toFloat()).toInt()

val Context.actionBarSize: Int
    get() {
        val attrs = theme.obtainStyledAttributes(intArrayOf(R.attr.actionBarSize))
        return attrs.getDimensionPixelSize(0, 0).also { attrs.recycle() }
    }
