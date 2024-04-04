package renetik.android.controller.view.grid

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import renetik.android.controller.base.CSView
import renetik.android.core.lang.CSLayoutRes
import renetik.android.core.lang.CSLayoutRes.Companion.layout
import renetik.android.ui.extensions.view.firstChild

// Requires item to be wrapped in empty frame layout for now...
open class CSGridItemView<RowType : Any>(
    parent: CSView<*>,
    group: ViewGroup,
    layout: CSLayoutRes,
    var onLoad: ((CSGridItemView<RowType>).(RowType) -> Unit)? = null
) : CSView<ViewGroup>(parent, group, layout) {

    constructor(
        parent: CSView<*>, group: ViewGroup, @LayoutRes layout: Int,
        onLoad: ((CSGridItemView<RowType>).(RowType) -> Unit)? = null
    ) : this(parent, group, layout.layout, onLoad)

    constructor(
        parent: CSView<out ViewGroup>, @LayoutRes layout: Int,
        onLoad: ((CSGridItemView<RowType>).(RowType) -> Unit)? = null
    ) : this(parent, parent.view, layout.layout, onLoad)

    constructor(
        parent: CSView<out ViewGroup>, layout: CSLayoutRes,
        onLoad: ((CSGridItemView<RowType>).(RowType) -> Unit)? = null
    ) : this(parent, parent.view, layout, onLoad)

    lateinit var value: RowType
    var index = -1
    var itemDisabled = false

    fun load(value: RowType, index: Int) {
        this.index = index
        this.value = value
        onLoad(value)
    }

    open fun onLoad(value: RowType) {
        onLoad?.invoke(this, value)
    }

    override val contentView get() = view.firstChild!!

    val isLoaded get() = index != -1
}

