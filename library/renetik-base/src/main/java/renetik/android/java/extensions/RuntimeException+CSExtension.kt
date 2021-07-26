package renetik.android.java.extensions

val unexpected get() = RuntimeException("Should not happen")
fun exception(message: String? = "") = RuntimeException(message)
fun unsupportedException(message: String? = "") = UnsupportedOperationException(message)
fun exception(exception: Exception) = RuntimeException(exception)
