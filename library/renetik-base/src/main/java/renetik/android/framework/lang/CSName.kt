package renetik.android.framework.lang

interface CSName {
    val name: String
}

fun CSName(name: String): CSName = CSNameImplementation(name)

private data class CSNameImplementation(override var name: String) : CSName {
    override fun toString() = name
}