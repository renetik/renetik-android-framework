package renetik.android.java.collections

class CSMapImpl<K, V> : java.util.HashMap<K, V>, CSMap<K, V> {

    constructor() {}

    constructor(map: Map<out K, V>) : super(map) {}

    override fun hasKey(key: K) = super.containsKey(key)

    override fun hasValue(value: V) = super.containsValue(value)

    override fun value(key: K) = super.get(key)

}
