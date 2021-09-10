package renetik.kotlin

fun Any?.equalsAny(vararg items: Any?): Boolean {
    for (item in items)
        if (this == item) return true
    return false
}

fun Any?.isAny(vararg items: Any?) = equalsAny(*items)

fun Any?.equalsAll(vararg items: Any?): Boolean {
    for (item in items)
        if (this != item) return false
    return true
}

fun Any?.isAll(vararg items: Any?) = equalsAll(*items)

fun <T : Any?> T.isTrue(predicate: (T) -> Boolean): Boolean = predicate(this)
fun <T : Any?> T.isFalse(predicate: (T) -> Boolean): Boolean = !predicate(this)

fun Any?.isOtherThen(vararg items: Any?) = !isAny(*items)