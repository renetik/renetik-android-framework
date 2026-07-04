package renetik.android.core.kotlin.primitives

import java.math.RoundingMode
import java.math.RoundingMode.CEILING
import java.text.DecimalFormat
import java.util.Locale.ENGLISH
import kotlin.math.pow

val Double.isWhole: Boolean get() = this % 1.0 == 0.0
val Double.Companion.Empty get() = MAX_VALUE
fun Double.roundToStep(step: Double): Double = (this / step).toInt() * step

fun Int.roundToStep(step: Int): Int = (this / step) * step

fun Double.formatDecimal(n: Int): Double {
    return "%.${n}f".format(ENGLISH, this).toDouble()
}

fun Double.formatRoundDecimal(
    format: String = "#.##", mode: RoundingMode = CEILING
): String = DecimalFormat(format).apply { roundingMode = mode }.format(this)

fun Double.pow(exponent: Int): Double = this.pow(exponent.toDouble())
