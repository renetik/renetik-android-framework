package renetik.android.controller.common

import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import renetik.android.framework.lang.CSLayoutRes.Companion.layout
import renetik.android.controller.R
import renetik.android.controller.base.CSDialogController
import renetik.android.controller.extensions.numberPicker
import renetik.android.controller.extensions.textView
import renetik.android.java.event.CSEventProperty
import renetik.android.java.extensions.collections.index
import renetik.android.view.extensions.circulate
import renetik.android.view.extensions.disableTextEditing
import renetik.android.view.extensions.loadData
import renetik.android.view.extensions.text


class CSItemPickerController<Row : Any>(
    @LayoutRes layout: Int = R.layout.cs_chooser,
    title: CharSequence, val data: List<Row>,
    selectedIndex: Int = 0, val onSelected: (Row) -> Unit
) : CSDialogController<LinearLayout>(layout(layout)) {

    constructor(
        @LayoutRes layout: Int = R.layout.cs_chooser, title: CharSequence,
        data: List<Row>, selected: Row, onSelected: (Row) -> Unit
    ) : this(
        layout = layout, title = title, data = data,
        selectedIndex = data.index(selected) ?: 0, onSelected = onSelected
    )

    constructor(
        @LayoutRes layout: Int, title: CharSequence,
        data: List<Row>, property: CSEventProperty<in Row>
    ) : this(layout = layout, title = title, data = data,
        selectedIndex = data.index(property.value) ?: 0, onSelected = { property.value = it })

    constructor(title: CharSequence, data: List<Row>, property: CSEventProperty<in Row>)
            : this(R.layout.cs_chooser, title, data, property)

    private val picker = numberPicker(R.id.cs_chooser_picker)

    init {
        textView(R.id.cs_chooser_text_title).text(title)
        picker.loadData(data, selectedIndex).circulate(false).disableTextEditing(true)
        onDismiss { onSelected(data[picker.value]) }
    }

    fun showWithOk() =
        show(R.string.cs_dialog_ok, onPositive = { onSelected(data[picker.value]) })

    fun showWithOkAndCancel() =
        show(onPositive = { onSelected(data[picker.value]) }, onNegative = { hide() })
}


