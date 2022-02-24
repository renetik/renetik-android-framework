package renetik.android.primitives

import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.Locale.ENGLISH

val Double.Companion.Empty get() = MAX_VALUE
fun Double.roundToStep(step: Double): Double = (this / step).toInt() * step

fun Int.roundToStep(step: Int): Int = (this / step) * step

fun Double.roundTo(n: Int): Double {
    return "%.${n}f".format(ENGLISH, this).toDouble()
}

fun Double.roundOffDecimal(format: String = "#.##"): Double? {
    val df = DecimalFormat(format)
    df.roundingMode = RoundingMode.CEILING
    return df.format(this).toDouble()
}
