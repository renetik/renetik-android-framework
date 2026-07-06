@file:Suppress("NOTHING_TO_INLINE")

package renetik.android.core.kotlin.primitives

import renetik.android.core.kotlin.ranges.first
import renetik.android.core.kotlin.ranges.size
import kotlin.math.roundToInt

inline fun Float.percentOf(size: Float): Float = this * size / 100f
inline fun Float?.percentOf(size: Float): Float? = this?.percentOf(size)
inline fun Float.percentOf(size: Int): Float = percentOf(size.toFloat())
inline fun Float?.percentOf(size: Int): Float? = this?.percentOf(size.toFloat())
inline fun Float.percentOf(size: Long): Float = percentOf(size.toFloat())
inline fun Float.percentOfInt(size: Float): Int = percentOf(size).toInt()
inline fun Float.percentOfInt(size: Int): Int = percentOf(size.toFloat()).toInt()

inline fun Float.percentOf(range: ClosedRange<Float>): Float {
    val size = range.size.takeIf { it > 0 } ?: return 0f
    return range.first + percentOf(size)
}

@JvmName("percentOfRangeInt")
inline fun Float.percentOf(range: ClosedRange<Int>): Float {
    val size = range.size.takeIf { it > 0 } ?: return 0f
    return range.first + percentOf(size)
}

inline fun Float.toPercentOf(total: Float): Float = when {
    total <= 0f || this <= 0f -> 0f
    this >= total -> 100f
    else -> this / total * 100f
}

inline fun Float.toPercentOf(total: Int): Float = toPercentOf(total.toFloat())
inline fun Float.toPercentOf(range: ClosedRange<Float>): Float =
    (this - range.first).toPercentOf(range.size)

inline fun Float.toPercentOfInt(total: Int): Int = toPercentOf(total).roundToInt()
inline fun Float.toPercentOfInt(range: ClosedRange<Float>): Int = toPercentOf(range).roundToInt()

inline fun Float.toPercentOfDouble(total: Int): Double = toPercentOfDouble(total.toDouble())
inline fun Float.toPercentOfDouble(total: Double): Double = when {
    total <= 0f || this <= 0f -> 0.0
    this >= total -> 100.0
    else -> this / total * 100.0
}