package renetik.android.framework.lang

interface CSSize {
    val size: Int
}

val CSSize.isEmpty get() = size == 0
val CSSize.notEmpty get() = size != 0