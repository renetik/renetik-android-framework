package renetik.java.extensions.primitives

val CharSequence?.set get() = this?.let { it.length > 0 } ?: false
val CharSequence?.empty get() = !set