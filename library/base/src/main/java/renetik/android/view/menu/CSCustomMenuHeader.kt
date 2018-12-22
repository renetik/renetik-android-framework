package renetik.android.view.menu

import renetik.android.java.collections.list

class CSCustomMenuHeader(val index: Int, val title: String) {

    val items = list<CSCustomMenuItem>()

    fun item(name: String) = items.put(CSCustomMenuItem(items.size, name))

    fun clear() = apply { items.clear() }
}
