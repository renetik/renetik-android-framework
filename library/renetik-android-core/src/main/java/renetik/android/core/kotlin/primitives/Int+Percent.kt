@file:Suppress("NOTHING_TO_INLINE")

package renetik.android.core.kotlin.primitives

import renetik.android.core.kotlin.ranges.first
import renetik.android.core.kotlin.ranges.size
import kotlin.math.roundToInt

inline fun Int.percentOf(size: Int): Float = (this * size / 100.0).toFloat()
inline fun Int.percentOf(size: Float): Float = this * size / 100f
inline fun Int.percentOf(size: Double): Double = this * size / 100.0
inline fun Int.percentOfInt(size: Int): Int = percentOf(size).toInt()
inline fun Int.percentOfInt(size: Float): Int = percentOf(size).toInt()
inline fun Int.percentOfInt(size: Double): Int = percentOf(size).toInt()
inline fun Int.percentOf(range: ClosedRange<Int>): Float {
    val size = range.size.takeIf { it > 0 } ?: return 0f
    return range.first + percentOf(size)
}

inline fun Int.toPercentOf(total: Float): Float {
    if (total == 0f) return 0f
    if (this <= 0f) return 0f
    if (this >= total) return 100f
    return (this / total * 100)
}

inline fun Int.toPercentOf(total: Double): Double {
    if (total == 0.0) return 0.0
    if (this <= 0f) return 0.0
    if (this >= total) return 100.0
    return (this / total * 100.0)
}

inline fun Int.toPercentOf(total: Int): Float = toPercentOf(total.toFloat())
inline fun Int.toPercentOfDouble(total: Int): Double = toPercentOf(total.toDouble())
inline fun Int.toPercentOfInt(total: Int): Int = toPercentOf(total).roundToInt()

inline fun Int.toPercentOf(range: ClosedRange<Int>): Float =
    (this - range.first).toPercentOf(range.size)
