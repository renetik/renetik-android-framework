package renetik.android.core.android.graphics

import android.content.Context
import android.graphics.Color
import android.graphics.Color.BLACK
import android.graphics.Color.BLUE
import android.graphics.Color.CYAN
import android.graphics.Color.DKGRAY
import android.graphics.Color.GRAY
import android.graphics.Color.GREEN
import android.graphics.Color.LTGRAY
import android.graphics.Color.MAGENTA
import android.graphics.Color.RED
import android.graphics.Color.TRANSPARENT
import android.graphics.Color.WHITE
import android.graphics.Color.YELLOW
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import renetik.android.core.lang.CSHasId

data class CSColor(@ColorInt val color: Int) : CSHasId {

    constructor(hex: String) : this(hex.toColorInt())

    fun toHex(): String = color.toHex()

    companion object {

        fun Int.toHex(): String = String.format("#%06X", this and 0xFFFFFF)

        val standard = listOf(
            BLACK, DKGRAY, GRAY, LTGRAY, WHITE, RED, GREEN,
            BLUE, YELLOW, CYAN, MAGENTA, TRANSPARENT
        ).map(::CSColor)


        fun generateLightColors(count: Int) = mutableListOf<Int>().apply {
            for (i in 0 until count) {
                val r = (200 + (Math.random() * 55)).toInt()
                val g = (200 + (Math.random() * 55)).toInt()
                val b = (200 + (Math.random() * 55)).toInt()
                this += Color.rgb(r, g, b)
            }
        }

        fun generateDarkColors(count: Int) = mutableListOf<Int>().apply {
            for (i in 0 until count) {
                val r = (0 + (Math.random() * 55)).toInt()
                val g = (0 + (Math.random() * 55)).toInt()
                val b = (0 + (Math.random() * 55)).toInt()
                this += Color.rgb(r, g, b)
            }
        }

        fun Context.colorRes(@ColorRes colorRes: Int) = CSColor(
            ContextCompat.getColor(this, colorRes))

        fun Context.colorInt(@ColorInt colorInt: Int) = CSColor(colorInt)
    }

    override val id: String = toHex()
}