package renetik.android.core.kotlin.primitives

import java.math.RoundingMode
import java.math.RoundingMode.CEILING
import java.text.DecimalFormat
import java.util.Locale.ENGLISH

val Double.Companion.Empty get() = MAX_VALUE
fun Double.roundToStep(step: Double): Double = (this / step).toInt() * step

fun Int.roundToStep(step: Int): Int = (this / step) * step

fun Double.formatRoundTo(n: Int): Double {
    return "%.${n}f".format(ENGLISH, this).toDouble()
}

fun Double.formatOffDecimal(format: String = "#.##",
                            mode: RoundingMode = CEILING): Double {
    val df = DecimalFormat(format)
    df.roundingMode = mode
    return df.format(this).toDouble()
}
