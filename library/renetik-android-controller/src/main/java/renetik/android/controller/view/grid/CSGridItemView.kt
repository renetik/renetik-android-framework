package renetik.android.controller.view.grid

import android.view.ViewGroup
import renetik.android.controller.base.CSView
import renetik.android.core.lang.CSLayoutRes
import renetik.android.ui.extensions.view.activated
import renetik.android.ui.extensions.view.firstChild
import renetik.android.ui.extensions.view.selected

// Requires item to be wrapped in empty frame layout for now...
open class CSGridItemView<RowType : Any>(
    parent: CSView<*>,
    group: ViewGroup,
    layout: CSLayoutRes,
    var onLoad: ((CSGridItemView<RowType>).(RowType) -> Unit)? = null)
    : CSView<ViewGroup>(parent, group, layout) {

    constructor(
        parent: CSView<out ViewGroup>, layout: CSLayoutRes,
        onLoad: ((CSGridItemView<RowType>).(RowType) -> Unit)? = null)
        : this(parent, parent.view, layout, onLoad)

    lateinit var value: RowType
    var index = -1
    var itemDisabled = false

    fun load(value: RowType, index: Int = 0) {
        this.index = index
        this.value = value
        onLoad(value)
    }

    open fun onLoad(value: RowType) {
        onLoad?.invoke(this, value)
    }

    val contentView = view.firstChild!!

    override var isActivated: Boolean
        get() = contentView.isActivated
        set(value) {
            contentView.activated(value)
        }

    override var isSelected: Boolean
        get() = contentView.isSelected
        set(value) {
            contentView.selected(value)
        }
}

