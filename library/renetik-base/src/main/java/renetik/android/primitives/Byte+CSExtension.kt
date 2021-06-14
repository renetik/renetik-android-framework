package renetik.android.primitives

fun Byte.toUnsigned() = if (this < 0) 256 + this else this.toInt()