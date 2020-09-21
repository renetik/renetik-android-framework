package renetik.android.java.common

//val <T : CSName> List<T>.asStringArray get() = map { it.name }.toTypedArray()

interface CSName {
    val name: String
}

fun CSName(name: String): CSName = CSNameImplementation(name)

private data class CSNameImplementation(override var name: String) : CSName {
    override fun toString() = name
}