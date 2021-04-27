package renetik.android.framework.lang

interface CSId {
    val id: String
}

fun CSId(id: String): CSId = CSIdImplementation(id)

private data class CSIdImplementation(override val id: String) : CSId