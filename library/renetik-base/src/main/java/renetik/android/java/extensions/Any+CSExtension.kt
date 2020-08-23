package renetik.android.java.extensions

import renetik.android.java.common.CSName
import renetik.android.task.CSDoLaterObject

fun Any?.asString(): String {
    val name = this as? CSName
    return name?.name ?: this?.toString() ?: ""
}

fun <T : Any> T.later(delayMilliseconds: Int = 0, function: (T).() -> Unit): T = apply {
    CSDoLaterObject.later(delayMilliseconds) { function(this) }
}

fun <T : Any> T.later(function: (T).() -> Unit): T = apply {
    CSDoLaterObject.later { function(this) }
}


