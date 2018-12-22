package renetik.android.java.collections

class CSLinkedMapImpl<K, V> : java.util.LinkedHashMap<K, V>, CSLinkedMap<K, V> {

    constructor(m: Map<out K, V>) : super(m) {}

    constructor() {}

    override fun getValue(index: Int) = list(values)[index]

    override fun index(key: K) = list(keys).indexOf(key)

    override fun hasKey(key: K) = super.containsKey(key)

    override fun hasValue(value: V) = super.containsValue(value)

    override fun value(key: K) = super.get(key)

    fun removeAt(i: Int) = remove(list(keys)[i])
}
