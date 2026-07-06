@file:Suppress("NOTHING_TO_INLINE")

package renetik.android.core.kotlin.primitives

import renetik.android.core.kotlin.ranges.first
import renetik.android.core.kotlin.ranges.size
import kotlin.math.roundToInt

inline fun Double.percentOf(size: Double): Double = this * size / 100f
inline fun Double?.percentOf(size: Double): Double? = this?.percentOf(size)
inline fun Double.percentOf(size: Int): Double = percentOf(size.toDouble())
inline fun Double?.percentOf(size: Int): Double? = this?.percentOf(size.toDouble())
inline fun Double.percentOf(size: Long): Double = percentOf(size.toDouble())
inline fun Double.percentOfInt(size: Double): Int = percentOf(size).toInt()
inline fun Double.percentOfInt(size: Int): Int = percentOf(size.toDouble()).toInt()

inline fun Double.percentOf(range: ClosedRange<Double>): Double {
    val size = range.size.takeIf { it > 0 } ?: return 0.0
    return range.first + percentOf(size)
}

@JvmName("percentOfRangeInt")
inline fun Double.percentOf(range: ClosedRange<Int>): Double {
    val size = range.size.takeIf { it > 0 } ?: return 0.0
    return range.first + percentOf(size)
}

inline fun Double.toPercentOf(total: Double): Double = when {
    total <= 0f || this <= 0f -> 0.0
    this >= total -> 100.0
    else -> this / total * 100.0
}

inline fun Double.toPercentOf(total: Int): Double = toPercentOf(total.toDouble())
inline fun Double.toPercentOf(range: ClosedRange<Double>): Double =
    (this - range.first).toPercentOf(range.size)
inline fun Double.toPercentOfInt(total: Int): Int = toPercentOf(total).roundToInt()