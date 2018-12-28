package renetik.android.extensions

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
