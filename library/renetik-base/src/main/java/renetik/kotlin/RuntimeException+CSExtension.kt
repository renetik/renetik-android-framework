package renetik.kotlin

val unexpected get() = CSUnexpectedException("Should not happen")

val unsupported get() = unsupported("Not supported")
fun unsupported(message: String? = "") = CSUnsupportedException(message)

fun exception(message: String? = "") = CSException(message)

open class CSException(message: String? = "") : RuntimeException(message)
class CSUnexpectedException(message: String? = "") : CSException(message)
class CSUnsupportedException(message: String? = "") : CSException(message)
