package renetik.java.extensions

fun string(value: Any?) = value.stringify()
fun Any?.stringify() = this?.toString() ?: ""

