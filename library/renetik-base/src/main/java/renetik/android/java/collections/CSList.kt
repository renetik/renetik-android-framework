package renetik.android.java.collections

import renetik.android.java.extensions.collections.putAll

fun <T> list(): ArrayList<T> = ArrayList()

fun <T> list(vararg items: T): ArrayList<T> = list<T>().putAll(*items)

fun <T> list(items: Iterable<T>): ArrayList<T> = list<T>().putAll(items)

//class CSList<T> : ArrayList<T>(), CSValues<T> {
//    override val values get() = this
//}