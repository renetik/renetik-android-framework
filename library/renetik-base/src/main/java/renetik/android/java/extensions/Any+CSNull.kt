package renetik.android.java.extensions

fun isAllSet(vararg items: Any?): Boolean {
    for (it in items) if (!it.isSet) return false
    return true
}

fun isAnyNotSet(vararg items: Any?) = !isAllSet(*items)

fun isAllNull(vararg items: Any?): Boolean {
    for (it in items) if (!it.isNull) return false
    return true
}

fun isAnyNotNull(vararg items: Any?) = !isAllNull(*items)

fun isAllNotNull(vararg items: Any?): Boolean {
    for (it in items) if (it.isNull) return false
    return true
}

fun isAnyNull(vararg items: Any?) = !isAllNotNull(*items)

fun <T : Any, R> T?.notNull(block: (T) -> R): R? = if (this != null) block(this) else null

val <T : Any> T?.notNull get() = this != null

fun <T : Any, R> T?.isNull(block: () -> R): R? = if (this == null) block() else null

val <T : Any> T?.isNull get() = this == null
