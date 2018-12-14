package renetik.java.collections

class CSListIterator<T>(private val list: MutableList<T>) : CSIterator<T>(list.size) {

    override val current: T get() = list[index()]

    override fun onRemove() {
        list.removeAt(index())
    }

}
