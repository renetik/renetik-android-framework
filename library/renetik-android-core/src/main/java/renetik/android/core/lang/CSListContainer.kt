package renetik.android.core.lang

interface CSListContainer<T> {
    val list: List<T>
}

fun <T : CSHasId> CSListContainer<T>.findById(id: String): T? {
    for (hasId in list) if (hasId.id == id) return hasId
    return null
}

fun <T : CSHasId> CSListContainer<T>.findById(hasId: CSHasId): T? {
    return findById(hasId.id)
}

fun <T : CSHasId> CSListContainer<T>.findIndexById(hasId: CSHasId): Int {
    return findIndexById(hasId.id)
}

fun <T : CSHasId> CSListContainer<T>.findIndexById(id: String): Int {
    for (index in 0 until list.size)
        if (list[index].id == id) return index
    return -1
}
