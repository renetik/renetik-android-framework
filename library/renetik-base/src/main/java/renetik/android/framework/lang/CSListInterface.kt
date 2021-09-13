package renetik.android.framework.lang

interface CSListInterface<T> : Iterable<T>, CSSize, Collection<T> {
    override val size: Int
    fun put(item: T): T
    fun delete(item: T): T
    fun removeAll(): CSListInterface<T>
    override fun isEmpty() = size == 0
    override operator fun contains(element: @UnsafeVariance T): Boolean {
        forEach { if (it == element) return true }
        return false
    }

    override fun containsAll(elements: Collection<@UnsafeVariance T>): Boolean {
        elements.forEach { if (!contains(it)) return false }
        return true
    }
}

class CSList<T> : ArrayList<T>, CSListInterface<T>, MutableCollection<T>, MutableList<T> {
    constructor() : super()
    constructor(collection: Collection<T>) : super(collection)

    override fun put(item: T): T {
        add(item); return item
    }

    override fun delete(item: T): T {
        remove(item); return item
    }

    override fun removeAll(): CSListInterface<T> {
        clear(); return this
    }

    override fun isEmpty() = super<ArrayList>.isEmpty()

    override operator fun contains(element: @UnsafeVariance T) =
        super<ArrayList>.contains(element)

    override fun containsAll(elements: Collection<@UnsafeVariance T>) =
        super<ArrayList>.containsAll(elements)
}