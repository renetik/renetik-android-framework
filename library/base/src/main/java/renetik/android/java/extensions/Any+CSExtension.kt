package renetik.android.java.extensions

fun string(value: Any?) = value.stringify()
fun Any?.stringify() = this?.toString() ?: ""

