package renetik.android.controller.view.grid.item

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import renetik.android.controller.view.grid.GridView
import renetik.android.core.lang.CSLayoutRes.Companion.layout
import renetik.android.event.property.CSProperty
import renetik.android.event.property.CSProperty.Companion.lateProperty
import renetik.android.ui.extensions.view.onClick

open class CSPropertyGridItemView<RowType : Any>(
    gridView: GridView<RowType>, group: ViewGroup,
    @LayoutRes val layout: Int,
    onLoad: ((CSGridItemView<RowType>).(RowType) -> Unit)? = null
) : CSGridItemView<RowType>(gridView, group, layout.layout, onLoad) {
    protected val property: CSProperty<RowType> = lateProperty()

    init {
        view.onClick { gridView.onItemClick(this) }
    }

    override fun onLoad(value: RowType) {
        property.value(value)
        super.onLoad(value)
    }
}