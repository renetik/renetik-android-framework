package renetik.android.java.common

val <T : CSName> List<T>.asStringArray get() = map { it.name }.toTypedArray()

interface CSName {
    val name: String
}

fun CSName(name: String): CSName = CSNameImplementation(name)

private data class CSNameImplementation(override var name: String) : CSName

open class CSNameValue<T>(override val name: String, override var value: T) : CSName, CSValue<T>