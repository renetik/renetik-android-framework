package renetik.android.java.extensions

import renetik.android.java.common.CSName
import renetik.android.task.CSDoLaterObject
import renetik.android.task.CSDoLaterObject.later

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

fun <T : Any> T.later(function: (T).() -> Unit): T = apply {
    CSDoLaterObject.later { function(this) }
}


