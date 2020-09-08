package renetik.android.java.extensions

import renetik.android.java.common.CSName
import renetik.android.java.common.CSValue
import renetik.android.task.CSDoLaterObject

fun Any?.asString(): String {
    return (this as? CSName)?.name
        ?: (this as? CSValue<*>)?.value as? String
        ?: this?.toString() ?: ""
}

fun <T : Any> T.later(delayMilliseconds: Int = 0, function: (T).() -> Unit): T = apply {
    CSDoLaterObject.later(delayMilliseconds) { function(this) }
}

fun <T : Any> T.later(function: (T).() -> Unit): T = apply {
    CSDoLaterObject.later { function(this) }
}

val <T : Any> List<T>.asStringArray get() = map { it.asString() }.toTypedArray()


