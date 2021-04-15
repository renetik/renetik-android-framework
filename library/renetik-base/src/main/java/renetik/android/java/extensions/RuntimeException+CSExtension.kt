package renetik.android.java.extensions

fun exception(message: String? = "") = RuntimeException(message)

fun unsupportedException(message: String? = "") = UnsupportedOperationException(message)

fun exception(exception: Exception) = RuntimeException(exception)