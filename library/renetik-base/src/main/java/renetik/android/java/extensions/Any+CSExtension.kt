package renetik.android.java.extensions

import renetik.android.task.CSDoLaterObject


fun <T : Any> T.later(delayMilliseconds: Int = 0, function: (T).() -> Unit): T = apply {
    CSDoLaterObject.later(delayMilliseconds) { function(this) }
}

fun <T : Any> T.later(function: (T).() -> Unit): T = apply {
    CSDoLaterObject.later { function(this) }
}