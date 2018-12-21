package renetik.java.collections

interface CSIteration<T> : MutableIterator<T>, MutableIterable<T> {
    fun index(): Int

    fun reverse(): CSIteration<T>

    fun skip(length: Int): CSIteration<T>
}