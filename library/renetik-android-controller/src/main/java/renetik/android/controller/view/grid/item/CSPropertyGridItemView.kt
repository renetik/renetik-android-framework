package renetik.android.controller.view.grid.item

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import renetik.android.controller.extensions.onClick
import renetik.android.controller.view.grid.GridView
import renetik.android.event.property.CSProperty
import renetik.android.event.property.CSProperty.Companion.lateProperty
import renetik.android.ui.extensions.view.onClick

open class CSPropertyGridItemView<RowType : Any>(
    gridView: GridView<RowType>, group: ViewGroup,
    @LayoutRes val layout: Int,
    onLoad: ((CSGridItemView<RowType>).(RowType) -> Unit)? = null
) : CSGridItemView<RowType>(gridView, group, layout, onLoad) {
    protected val property: CSProperty<RowType> = lateProperty()

    init {
        onClick { gridView.onItemClick(this) }
    }

    override fun onLoad(data: RowType) {
        property.value(data)
        super.onLoad(data)
    }
}