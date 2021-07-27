package renetik.android.framework.lang

// Can this be removed ? Looks like not so needed anymore in kotlin
interface CSTitle {
    val title: String
}

fun CSTitle(name: String): CSTitle = CSTitleImplementation(name)

private data class CSTitleImplementation(override var title: String) : CSTitle {
    override fun toString() = title
}