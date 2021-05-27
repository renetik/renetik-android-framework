package renetik.android.primitives

import kotlin.random.Random

val Int.Companion.empty get() = Int.MAX_VALUE
fun Int.Companion.random(min: Int, max: Int): Int {
    if (min >= max) throw IllegalArgumentException("max must be greater than min")
    return Random.nextInt(max - min + 1) + min
}

val Int.isEmpty get() = this == Int.empty
val Int.isSet get() = !this.isEmpty
fun Int.max(maximumValue: Int) = if (this < maximumValue) this else maximumValue
fun Int.min(minimumValue: Int) = if (this > minimumValue) this else minimumValue
