package renetik.android.controller.menu

import android.view.MenuItem
import renetik.android.core.kotlin.primitives.isSet

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
        if (isConsumed) return false
        if (item.id.isSet) isConsumed = this.item.itemId == item.id
        return isConsumed
    }

    fun consume(id: Int): Boolean {
        if (isConsumed) return false
        isConsumed = item.itemId == id
        return isConsumed
    }

}