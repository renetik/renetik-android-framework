package renetik.android.java.extensions

fun <T : Any, R> T?.notNull(block: (T) -> R): R? = if (this != null) block(this) else null

val <T : Any> T?.notNull get() = this != null

fun <T : Any, R> T?.isNull(block: () -> R): R? = if (this == null) block() else null

val <T : Any> T?.isNull get() = this == null
