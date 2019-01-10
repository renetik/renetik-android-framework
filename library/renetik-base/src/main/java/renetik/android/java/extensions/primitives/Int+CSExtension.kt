package renetik.android.java.extensions.primitives

import kotlin.random.Random

fun randomIntInRange(min: Int, max: Int): Int {
    if (min >= max)
        throw IllegalArgumentException("max must be greater than min")
    return Random.nextInt(max - min + 1) + min
}