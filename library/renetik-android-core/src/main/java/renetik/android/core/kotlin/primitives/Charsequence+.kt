package renetik.android.core.kotlin.primitives

val CharSequence?.isSet get() = this?.isNotEmpty() ?: false
val CharSequence?.isEmpty get() = !isSet