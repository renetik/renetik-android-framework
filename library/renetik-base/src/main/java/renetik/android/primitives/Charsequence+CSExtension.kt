package renetik.android.java.extensions.primitives

val CharSequence?.isSet get() = this?.isNotEmpty() ?: false
val CharSequence?.isEmpty get() = !isSet