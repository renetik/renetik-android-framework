package renetik.android.controller.common

import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import renetik.android.controller.R
import renetik.android.controller.base.CSActivityView
import renetik.android.controller.base.CSDialogView
import renetik.android.controller.base.numberPicker
import renetik.android.controller.base.textView
import renetik.android.framework.event.property.CSEventProperty
import renetik.android.framework.lang.CSLayoutRes.Companion.layout
import renetik.android.java.extensions.collections.index
import renetik.android.view.extensions.circulate
import renetik.android.view.extensions.disableTextEditing
import renetik.android.view.extensions.loadData
import renetik.android.view.extensions.text


class CSItemPickerView<Row : Any>(
    parent: CSActivityView<out ViewGroup>,
    @LayoutRes layout: Int = R.layout.cs_chooser,
    title: CharSequence, val data: List<Row>,
    selectedIndex: Int = 0, val onSelected: (Row) -> Unit
) : CSDialogView<LinearLayout>(parent, layout(layout)) {

    constructor(
        parent: CSActivityView<out ViewGroup>,
        @LayoutRes layout: Int = R.layout.cs_chooser, title: CharSequence,
        data: List<Row>, selected: Row, onSelected: (Row) -> Unit
    ) : this(
        parent,
        layout = layout, title = title, data = data,
        selectedIndex = data.index(selected) ?: 0, onSelected = onSelected
    )

    constructor(
        parent: CSActivityView<out ViewGroup>,
        @LayoutRes layout: Int, title: CharSequence,
        data: List<Row>, property: CSEventProperty<in Row>
    ) : this(parent, layout = layout, title = title, data = data,
        selectedIndex = data.index(property.value) ?: 0, onSelected = { property.value = it })

    constructor(parent: CSActivityView<out ViewGroup>,
                title: CharSequence,
                data: List<Row>,
                property: CSEventProperty<in Row>)
            : this(parent, R.layout.cs_chooser, title, data, property)

    private val picker = numberPicker(R.id.cs_chooser_picker)

    init {
        textView(R.id.cs_chooser_text_title).text(title)
        picker.loadData(data, selectedIndex).circulate(false).disableTextEditing(true)
        onDismiss { onSelected(data[picker.value]) }
    }

    fun showWithOk() =
        show(R.string.cs_dialog_ok, onPositive = { onSelected(data[picker.value]) })

    fun showWithOkAndCancel() =
        show(onPositive = { onSelected(data[picker.value]) }, onNegative = { dismiss() })
}


