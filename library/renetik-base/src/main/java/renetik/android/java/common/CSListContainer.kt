package renetik.android.java.common

interface CSListContainer<T> {
    val list: List<T>
}

fun <T : CSId> CSListContainer<T>.findById(id: String): T? {
    for (hasId in list) if (hasId.id == id) return hasId
    return null
}

fun <T : CSId> CSListContainer<T>.findById(hasId: CSId): T? {
    return findById(hasId.id)
}

fun <T : CSId> CSListContainer<T>.findIndexById(hasId: CSId): Int {
    return findIndexById(hasId.id)
}

fun <T : CSId> CSListContainer<T>.findIndexById(id: String): Int {
    for (index in 0 until list.size)
        if (list[index].id == id) return index
    return -1
}
