package renetik.android.core.kotlin.collections

import renetik.android.core.kotlin.primitives.removeAccents

fun List<String>.filterIgnoreAccents(constraint: CharSequence? = null): List<String> {
    val query = constraint?.toString()?.removeAccents()?.trim()
    return if (query.isNullOrBlank()) this
    else this.filter { it.removeAccents().contains(query, ignoreCase = true) }
}

fun List<String>.filter(constraint: CharSequence? = null): List<String> {
    val query = constraint?.toString()?.trim()
    return if (query.isNullOrBlank()) this
    else this.filter { it.removeAccents().contains(query, ignoreCase = true) }
}
