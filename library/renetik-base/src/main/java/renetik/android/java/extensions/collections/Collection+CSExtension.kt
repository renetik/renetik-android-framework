package renetik.android.java.extensions.collections

fun <T> Collection<T>.containsNot(element: T) = !contains(element)
