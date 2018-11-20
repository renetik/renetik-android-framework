package renetik.android.view.menu

import android.view.MenuItem
import renetik.java.extensions.set
import renetik.java.lang.CSLang

class CSOnMenuItem(private val item: MenuItem) {
    var isConsumed = false

    val isCheckable
        get() = item.isCheckable

    var isChecked
        get() = item.isChecked
        set(value) {
            item.isChecked = value
        }


    fun consume(item: CSMenuItem): Boolean {
        if (isConsumed) return CSLang.NO
        if (set(item.id)) isConsumed = this.item.itemId == item.id
        return isConsumed
    }

    fun consume(id: Int): Boolean {
        if (isConsumed) return CSLang.NO
        isConsumed = item.itemId == id
        return isConsumed
    }

}