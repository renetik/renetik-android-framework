package renetik.android.controller.view.grid.item

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import renetik.android.controller.base.CSView
import renetik.android.core.lang.CSLayoutRes.Companion.layout
import renetik.android.ui.extensions.view.firstChild

//TODO: Requires item to be wrapped in empty frame layout for now... ?
// It was like that for some reselection .. Now it is differently done in other places..
open class CSGridItemView<RowType : Any>(
    parent: CSView<*>,
    group: ViewGroup,
    @LayoutRes layout: Int,
    var onLoad: ((CSGridItemView<RowType>).(RowType) -> Unit)? = null
) : CSView<ViewGroup>(parent, group, layout.layout) {

    constructor(
        parent: CSView<out ViewGroup>, @LayoutRes layout: Int,
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

    open fun onLoad(data: RowType) {
        onLoad?.invoke(this, data)
    }

    override val contentView get() = view.firstChild!!

    val isLoaded get() = index != -1
}