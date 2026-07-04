package renetik.android.core.android.graphics

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils

val @receiver:ColorInt Int.darkenMore: Int
    @ColorInt
    get() {
        val floatArray = FloatArray(3)
        Color.colorToHSV(this, floatArray)
        floatArray[2] *= 0.8f
        return Color.HSVToColor(floatArray)
    }

val @receiver:ColorInt Int.darken
    @ColorInt
    get() = ColorUtils.blendARGB(this, Color.BLACK, 0.2f)

fun @receiver:ColorInt Int.setAlpha(alpha: Float): Int {
    require(alpha in 0.0..1.0) { "Alpha must be between 0.0 and 1.0" }
    val alphaInt = alpha.alphaInt and 0xFF
    return (this and 0x00FFFFFF) or (alphaInt shl 24)
}

val Float.alphaInt: Int get() = (coerceIn(0f, 1f) * 255).toInt()