package renetik.android.framework.lang

// Can this be removed ? Looks like not so needed anymore in kotlin
interface CSName {
    val name: String
}

fun CSName(name: String): CSName = CSNameImplementation(name)

private data class CSNameImplementation(override var name: String) : CSName {
    override fun toString() = name
}