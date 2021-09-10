package renetik.kotlin

val unexpected get() = CSUnexpectedException.unexpected()
fun unexpected(message: String? = "") = CSUnexpectedException.unexpected(message)
class CSUnexpectedException(message: String? = "") : CSException(message) {
    companion object {
        val unexpected get() = unexpected()
        fun unexpected(message: String? = "") = CSUnexpectedException("Unexpected $message")
    }
}

val unsupported get() = CSUnsupportedException.unsupported()
fun unsupported(message: String? = "") = CSUnsupportedException.unsupported(message)
class CSUnsupportedException(message: String? = "") : CSException(message) {
    companion object {
        val unsupported get() = unsupported()
        fun unsupported(message: String? = "") = CSUnsupportedException("Unsupported $message")
    }
}

fun exception(message: String? = "") = CSException(message)
open class CSException(message: String? = "") : RuntimeException(message)

