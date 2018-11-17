package renetik.android.viewbase.menu

import android.view.MenuItem
import renetik.android.lang.CSLang.NO
import renetik.android.lang.CSLang.set

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
        if (isConsumed) return NO
        if (set(item.id)) isConsumed = this.item.itemId == item.id
        return isConsumed
    }

    fun consume(id: Int): Boolean {
        if (isConsumed) return NO
        isConsumed = item.itemId == id
        return isConsumed
    }

}