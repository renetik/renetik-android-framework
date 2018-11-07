package renetik.android.viewbase.menu

import android.view.MenuItem
import renetik.android.lang.CSLang.NO
import renetik.android.lang.CSLang.set
import renetik.java.lang.CSValue

class CSOnMenuItem(private val item: MenuItem) {
    val consumed = CSValue(false)

    val isCheckable
        get() = item.isCheckable

    var isChecked
        get() = item.isChecked
        set(value) {
            item.isChecked = value
        }


    fun consume(item: CSMenuItem): Boolean {
        if (consumed.value) return NO
        if (set(item.id)) consumed.value = this.item.itemId == item.id
        return consumed.value
    }

    fun consume(id: Int): Boolean {
        if (consumed.value) return NO
        consumed.value = item.itemId == id
        return consumed.value
    }

}