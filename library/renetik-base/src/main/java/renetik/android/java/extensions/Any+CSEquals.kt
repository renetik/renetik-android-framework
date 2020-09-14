package renetik.android.java.extensions

fun Any?.equalsAny(vararg items: Any?): Boolean {
    for (item in items)
        if (this == item) return true
    return false
}

fun Any?.equalsAll(vararg items: Any?): Boolean {
    for (item in items)
        if (this != item) return false
    return true
}

fun <T : Any?> T.isTrue(predicate: (T) -> Boolean): Boolean = predicate(this)