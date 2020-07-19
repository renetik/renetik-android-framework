package renetik.android.java.common

val <T : CSName> List<T>.asStringArray get() = map { it.name }.toTypedArray()

interface CSName {
    val name: String
}

fun CSNameValue(name: String) = CSNameValue(name, name)

class CSNameValue<T>(override val name: String, val value: T) : CSName {
}