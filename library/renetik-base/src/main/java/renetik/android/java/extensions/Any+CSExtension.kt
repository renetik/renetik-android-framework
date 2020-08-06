package renetik.android.java.extensions

import renetik.android.java.common.CSName

object AnyCSExtension {
    fun string(value: Any?) = value.stringify()
}

fun Any?.stringify(): String {
    val name = this as? CSName
    return name?.name ?: this?.toString() ?: ""
}

val Any?.asString get() = stringify()

fun <T> Any.privateField(name: String): T {
    val field = this::class.java.getDeclaredField(name)
    field.isAccessible = true
    @Suppress("UNCHECKED_CAST")
    return field.get(this) as T
}


