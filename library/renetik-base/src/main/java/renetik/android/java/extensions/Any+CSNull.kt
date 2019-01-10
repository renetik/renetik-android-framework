package renetik.android.java.extensions

fun notNull(vararg items: Any?): Boolean {
    for (it in items) if (it.isNull) return false
    return true
}

fun isSet(vararg items: Any?) = notNull(*items)

fun notSet(vararg items: Any?) = !isSet(*items)

fun isNull(vararg items: Any?): Boolean = !notNull(*items)

fun <T : Any, R> T?.notNull(block: (T) -> R): R? = if (this != null) block(this) else null

val <T : Any> T?.notNull get() = this != null

fun <T : Any, R> T?.isNull(block: () -> R): R? = if (this == null) block() else null

val <T : Any> T?.isNull get() = this == null
