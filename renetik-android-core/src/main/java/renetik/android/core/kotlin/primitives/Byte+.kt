package renetik.android.core.kotlin.primitives

val Byte.asUnsignedInt: Int get() = toInt() and 0xFF

infix fun Byte.and(that: Int): Int = toInt().and(that)