package renetik.android.java.common

interface CSValues<T> {
    val values: MutableList<T>
}

fun <T : CSId> CSValues<T>.findById(id: String): T? {
    for (hasId in values) if (hasId.id() == id) return hasId
    return null
}

fun <T : CSId> CSValues<T>.findById(hasId: CSId): T? {
    return findById(hasId.id())
}

fun <T : CSId> CSValues<T>.findIndexById(hasId: CSId): Int {
    return findIndexById(hasId.id())
}

fun <T : CSId> CSValues<T>.findIndexById(id: String): Int {
    for (index in 0 until values.size)
        if (values[index].id() == id)
            return index
    return -1
}
