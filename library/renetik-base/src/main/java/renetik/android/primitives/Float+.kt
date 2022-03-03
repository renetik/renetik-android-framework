package renetik.android.primitives

import java.math.RoundingMode
import java.math.RoundingMode.CEILING
import java.text.DecimalFormat
import java.util.*

val Float.Companion.Empty get() = MAX_VALUE
val Float.isEmpty get() = this == Float.Empty
val Float.isSet get() = !this.isEmpty
fun Float.ifEmpty(function: (Float) -> Unit) = apply {
    if (this.isEmpty) function(this)
}

fun Float.ifSet(function: (Float) -> Unit) = apply {
    if (this.isSet) function(this)
}

fun Float.roundToStep(step: Float): Float = (this / step).toInt() * step
fun Float.roundToStep(step: Double): Double = (this / step).toInt() * step
fun Float.roundToStep(step: Int): Int = (this / step).toInt() * step

fun Float.formatRoundTo(n: Int): String {
    return "%.${n}f".format(Locale.ENGLISH, this)
}

fun Float.formatOffDecimal(format: String = "#.##",
                           mode: RoundingMode = CEILING): String {
    val df = DecimalFormat(format)
    df.roundingMode = mode
    return df.format(this)
}

