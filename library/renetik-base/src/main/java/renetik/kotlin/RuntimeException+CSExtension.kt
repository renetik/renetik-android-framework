package renetik.kotlin

val unfinished get() = unexpected("This code is unfinished, contact developer")
fun unfinished(): Nothing = throw unexpected()
val unexpected get() = CSUnexpectedException.unexpected()
val impossible get() = unexpected("Path that leads to this situation should not be possible")
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

