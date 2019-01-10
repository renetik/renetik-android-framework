package renetik.android.java.collections

//package renetik.android.java.collections.collections
//
//import renetik.android.java.extensions.exception
//
//abstract class CSIterator<T>(private var iterationLength: Int) : CSIteration<T> {
//    private var index = -1
//    private var iteratingForward = true
//
//    abstract val current: T
//
//    override fun hasNext(): Boolean {
//        return if (iteratingForward) index < iterationLength - 1 else index > 0
//    }
//
//    override fun index() = index
//
//    override fun iterator() = this
//
//    override fun next(): T {
//        if (iteratingForward) ++index
//        else --index
//        return current
//    }
//
//    protected open fun onRemove() {
//        throw exception("Not implemented")
//    }
//
//    override fun remove() {
//        onRemove()
//        index--
//        iterationLength--
//    }
//
//    override fun reverse(): CSIteration<T> {
//        this.iteratingForward = false
//        index = iterationLength
//        return this
//    }
//
//    override fun skip(length: Int): CSIteration<T> {
//        if (iteratingForward)
//            index += length
//        else
//            index -= length
//        return this
//    }
//}