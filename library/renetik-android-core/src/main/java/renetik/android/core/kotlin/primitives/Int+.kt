@file:Suppress("NOTHING_TO_INLINE")

package renetik.android.core.kotlin.primitives

import renetik.android.core.android.content.dpToPixel
import renetik.android.core.android.content.dpToPixelF
import renetik.android.core.android.content.spToPixelF
import renetik.android.core.android.content.toDp
import renetik.android.core.android.content.toDpF
import renetik.android.core.lang.ArgFun
import renetik.android.core.lang.CSEnvironment.app
import renetik.android.core.lang.CSTimeConstants.Minute
import renetik.android.core.lang.CSTimeConstants.Second
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.absoluteValue
import kotlin.random.Random

inline operator fun Int.minus(other: Int?): Int = this - (other ?: 0)
inline operator fun Int.plus(other: Int?): Int = this + (other ?: 0)


private val counter = AtomicInteger(1)
fun Int.Companion.unique(): Int =
    counter.getAndUpdate { if (it == MAX_VALUE) 1 else it + 1 }

inline fun Int.Companion.random(min: Int = 0, max: Int = MAX_VALUE): Int {
    if (min >= max) throw IllegalArgumentException("max must be greater than min")
    return Random.nextInt(max - min + 1) + min
}

inline fun Int.max(maximumValue: Int) = coerceAtMost(maximumValue)
inline fun Int.min(minimumValue: Int) = coerceAtLeast(minimumValue)

inline fun Long.max(maximumValue: Long) = coerceAtMost(maximumValue)
inline fun Long.min(minimumValue: Long) = coerceAtLeast(minimumValue)

inline infix fun Int.isFlagSet(bitwise: Int) = bitwise and this != 0
inline infix fun Int.isFlagNotSet(bitwise: Int) = !this.isFlagSet(bitwise)

inline val Int.isFirstIndex get() = this == 0
inline val Int.asIndex get() = this - 1
inline fun Int.isLastIndex(index: Int) = index == this - 1

inline val Int.isEven: Boolean get() = this % 2 == 0
inline val Int.isOdd: Boolean get() = !isEven

fun Int.update(
    newCount: Int, onAdd: ArgFun<Int>? = null, onRemove: ArgFun<Int>? = null,
) {
    val lastIndex = this - 1
    val difference = newCount - this
    if (difference > 0) repeat(difference) { onAdd?.invoke(lastIndex + 1 + it) }
    else repeat(difference.absoluteValue) { onRemove?.invoke(lastIndex - it) }
}

inline val Int.dp: Int get() = app.dpToPixel(this)
inline val Int.px: Int get() = app.toDp(this)
inline val Int.dpf: Float get() = app.dpToPixelF(this)
inline val Int.spf: Float get() = app.spToPixelF(this)
inline val Int.pxf: Float get() = app.toDpF(this)
inline val Int.second: Int get() = this * Second
inline val Int.minute: Int get() = this * Minute

inline fun Int.isPowerOfTwo(): Boolean =
    this > 0 && (this and (this - 1)) == 0

inline fun Int.nextPowerOfTwo(): Int {
    if (this <= 0) return 1
    return if (this and (this - 1) == 0) this
    else Integer.highestOneBit(this) shl 1
}

inline fun Int.nearestPowerOfTwo(): Int {
    if (this <= 0) return 1
    return Integer.highestOneBit(this)
}

/**
 * Integer exponentiation.
 * @param exponent must be ≥ 0
 * @return this raised to the given exponent
 * @throws IllegalArgumentException if exponent is negative
 */
inline fun Int.pow(exponent: Int): Int {
    require(exponent >= 0) { "Exponent must be non-negative, was $exponent" }
    var result = 1
    var base = this
    var exp = exponent
    while (exp > 0) {
        if (exp and 1 == 1) {
            result *= base
        }
        base *= base
        exp = exp shr 1
    }
    return result
}
