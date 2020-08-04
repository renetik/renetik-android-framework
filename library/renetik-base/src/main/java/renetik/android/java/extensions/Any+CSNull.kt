package renetik.android.java.extensions

fun isSet(vararg items: Any?): Boolean {
    for (it in items) if (!it.isSet) return false
    return true
}

fun isSomeNotSet(vararg items: Any?) = !isSet(*items)

fun isNull(vararg items: Any?): Boolean {
    for (it in items) if (!it.isNull) return false
    return true
}

fun isSomeNotNull(vararg items: Any?) = !isNull(*items)

fun isNotNull(vararg items: Any?): Boolean {
    for (it in items) if (it.isNull) return false
    return true
}

fun isSomeNull(vararg items: Any?) = !isNotNull(*items)

fun <T : Any, R> T?.notNull(block: (T) -> R): R? = if (this != null) block(this) else null

val <T : Any> T?.notNull get() = this != null

fun <T : Any, R> T?.isNull(block: () -> R): R? = if (this == null) block() else null

val <T : Any> T?.isNull get() = this == null
