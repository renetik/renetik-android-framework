package renetik.android.java.common

import renetik.android.java.collections.list

private fun <T : CSName> List<T>.toNames() = list<String>().apply {
    for (hasName in this@toNames) add(hasName.name())
}

interface CSName {
    fun name(): String
}
