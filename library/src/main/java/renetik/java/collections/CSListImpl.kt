package renetik.java.collections

import java.util.*
import java.util.Arrays.asList

class CSListImpl<T> : ArrayList<T>, CSList<T> {

    override val hasItems get() = size > 0

    override val values get() = this

    constructor() : super() {}
    constructor(collection: Collection<T>) : super(collection) {}
    constructor(capacity: Int) : super(capacity) {}
    constructor(list: List<T>) : super(list) {}

    override fun at(index: Int): T? = if (index < size && index >= 0) get(index) else null
    override fun first(): T? = at(0)
    override fun second(): T? = at(1)
    override fun isLast(item: T) = last() === item
    override fun isLastIndex(index: Int) = index == size - 1
    override fun last() = at(lastIndex())
    override fun previousLast() = at(lastIndex() - 1)
    override fun lastIndex() = size - 1
    override fun rangeFrom(fromIndex: Int) = range(fromIndex, size)
    override fun range(fromIndex: Int, toIndex: Int) = CSListImpl(subList(fromIndex, toIndex))
    override fun deleteLast() = if (isEmpty()) null else removeAt(size - 1)
    override fun reverse() = apply { Collections.reverse(this) }

    override fun put(item: T): T {
        add(item)
        return item
    }

    override fun putAll(items: Iterable<T>) = apply { for (item in items) add(item) }
    override fun putAll(vararg items: T) = putAll(asList(*items))

    override fun put(item: T, index: Int): T {
        add(index, item)
        return item
    }

    override fun replace(item: T, index: Int): T {
        set(index, item)
        return item
    }

    override fun reload(values: Iterable<T>) = apply {
        clear()
        putAll(values)
    }

    override fun delete(item: T): Int {
        val index = indexOf(item)
        removeAt(index)
        return index
    }

    override fun delete(index: Int): T? = at(index)?.apply { super.removeAt(index) }

    override fun deleteAll() = apply { clear() }
}
