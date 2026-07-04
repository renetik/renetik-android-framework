package renetik.android.core.kotlin.collections

fun <T> List<T>.rangeFrom(index: Int): List<T> = subList(index, size)
fun <T> List<T>.rangeTo(toIndex: Int): List<T> = subList(0, toIndex)
fun <T> List<T>.range(fromIndex: Int, toIndex: Int): List<T> =
    subList(fromIndex, toIndex)

fun <T> List<T>.rangeFrom(from: (T) -> Boolean): List<T> {
    val indexOfFirst = indexOfFirst(from)
    if (indexOfFirst != -1) return rangeFrom(indexOfFirst)
    return emptyList()
}

fun <T> List<T>.rangeTo(to: (T) -> Boolean): List<T> {
    val indexOfLast = indexOfLast(to)
    if (indexOfLast != -1) return rangeTo(indexOfLast + 1)
    return emptyList()
}

fun <T> List<T>.range(from: (T) -> Boolean, to: (T) -> Boolean): List<T> {
//    val indexOfFirst = indexOfFirst(from)
//    val indexOfLast = indexOfLast(to)
//    if (indexOfFirst != -1 && indexOfLast != -1)
//        return range(fromIndex = indexOfFirst, toIndex = indexOfLast + 1)
//    if (indexOfFirst == -1) return rangeTo(indexOfLast)
    return rangeFrom(from).rangeTo(to)
}