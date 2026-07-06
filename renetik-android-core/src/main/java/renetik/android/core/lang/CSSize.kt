package renetik.android.core.lang

interface CSSize {
    val size: Int
}

val CSSize.isEmpty get() = size == 0
val CSSize.notEmpty get() = size != 0