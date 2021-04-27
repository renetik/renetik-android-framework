package renetik.android.primitives

import kotlin.random.Random

object CSInt {
    fun random(min: Int, max: Int): Int {
        if (min >= max)
            throw IllegalArgumentException("max must be greater than min")
        return Random.nextInt(max - min + 1) + min
    }
}

fun Int.max(maximumValue: Int) = if (this < maximumValue) this else maximumValue
fun Int.min(minimumValue: Int) = if (this > minimumValue) this else minimumValue