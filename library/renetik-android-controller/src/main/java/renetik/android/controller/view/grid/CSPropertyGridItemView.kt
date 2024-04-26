package renetik.android.controller.view.grid

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import renetik.android.controller.base.CSView
import renetik.android.core.lang.CSLayoutRes.Companion.layout
import renetik.android.event.property.CSProperty
import renetik.android.event.property.CSProperty.Companion.lateProperty

open class CSPropertyGridItemView<RowType : Any>(
    parent: CSView<*>,
    group: ViewGroup,
    @LayoutRes val layout: Int,
    onLoad: ((CSGridItemView<RowType>).(RowType) -> Unit)? = null
) : CSGridItemView<RowType>(parent, group, layout.layout, onLoad) {
    protected val property: CSProperty<RowType> = lateProperty()
    override fun onLoad(value: RowType) {
        property.value(value)
        super.onLoad(value)
    }
}