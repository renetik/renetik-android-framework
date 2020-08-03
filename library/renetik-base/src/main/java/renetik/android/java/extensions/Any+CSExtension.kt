package renetik.android.java.extensions

import renetik.android.java.common.CSName

object AnyCSExtension {
    fun string(value: Any?) = value.stringify()
}

inline fun <T> T.self(block: T.() -> Unit) = apply(block)

fun Any?.stringify(): String {
    val name = this as? CSName
    return name?.name ?: this?.toString() ?: ""
}

val Any?.asString get() = stringify()
