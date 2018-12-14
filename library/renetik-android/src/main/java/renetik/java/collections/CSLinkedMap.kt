package renetik.java.collections

interface CSLinkedMap<K, V> : CSMap<K, V> {
    fun getValue(index: Int): V
    fun index(key: K): Int
}
