package renetik.android.controller.view.grid

import android.view.ViewGroup
import androidx.core.view.children
import renetik.android.controller.base.CSView
import renetik.android.framework.lang.CSLayoutRes
import renetik.android.view.activatedIf
import renetik.android.view.selectedIf

// Requires item to be wrapped in empty frame layout for now...
open class CSGridItemView<RowType : Any>(
    parent: CSView<out ViewGroup>,
    layout: CSLayoutRes,
    var onLoad: ((CSGridItemView<RowType>).(RowType) -> Unit)? = null)
    : CSView<ViewGroup>(parent, layout) {

    lateinit var row: RowType
    var index = -1
    var itemDisabled = false

    fun load(row: RowType, index: Int = 0) {
        this.index = index
        this.row = row
        onLoad(row)
    }

    open fun onLoad(row: RowType) {
        onLoad?.invoke(this, row)
    }

    override var isActivated: Boolean
        get() = view.children.first().isActivated
        set(value) = view.children.first().activatedIf(value)

    override var isSelected: Boolean
        get() = view.children.first().isSelected
        set(value) = view.children.first().selectedIf(value)
}

