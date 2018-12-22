package renetik.android.java.extensions

fun exception(message: String) = RuntimeException(message)
fun exception(ex: Exception) = RuntimeException(ex)