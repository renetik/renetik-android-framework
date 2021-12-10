package renetik.kotlin

import renetik.kotlin.CSUnexpectedException.Companion.unexpected
import renetik.kotlin.CSUnsupportedException.Companion.unsupported

fun unfinished(message: String = "This code is unfinished, contact developer"): Nothing =
    throw unexpected(message)

fun impossible(message: String? = "Obviously impossible but..."): Nothing =
    throw unexpected(message)

fun unexpected(message: String? = "Not expected to happen"): Nothing =
    throw unexpected(message)

class CSUnexpectedException(message: String? = "") : CSException(message) {
    companion object {
        val unexpected get() = unexpected()
        fun unexpected(message: String? = "") = CSUnexpectedException("Unexpected $message")
    }
}

fun unsupported(message: String? = "Not supported to call this right now"): Nothing =
    throw  unsupported(message)

class CSUnsupportedException(message: String? = "") : CSException(message) {
    companion object {
        val unsupported get() = unsupported()
        fun unsupported(message: String? = "") = CSUnsupportedException("Unsupported $message")
    }
}

fun exception(message: String? = "") = CSException(message)
open class CSException(message: String? = "") : RuntimeException(message)

