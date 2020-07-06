package renetik.android.java.common

val <T : CSName> List<T>.asStringArray get() = map { it.name }.toTypedArray()

interface CSName {
    val name: String
}

class CSNameValue(override val name: String) : CSName