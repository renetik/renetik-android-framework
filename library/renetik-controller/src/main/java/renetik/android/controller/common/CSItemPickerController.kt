package renetik.android.controller.common

import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import renetik.android.base.layout
import renetik.android.controller.R
import renetik.android.controller.base.CSDialogController
import renetik.android.extensions.numberPicker
import renetik.android.extensions.textView
import renetik.android.java.common.CSName
import renetik.android.java.event.CSEventProperty
import renetik.android.java.event.listener
import renetik.android.java.extensions.collections.index
import renetik.android.view.extensions.circulate
import renetik.android.view.extensions.disableTextEditing
import renetik.android.view.extensions.loadData
import renetik.android.view.extensions.text

class CSItemPickerController<Row : CSName>(@LayoutRes layout: Int = R.layout.cs_item_picker,
                                           title: CharSequence, val data: List<Row>,
                                           selectedIndex: Int = 0, val onSelected: (Row) -> Unit)
    : CSDialogController<LinearLayout>(layout(layout)) {

    constructor(@LayoutRes layout: Int = R.layout.cs_item_picker, title: CharSequence,
                data: List<Row>, selected: Row, onSelected: (Row) -> Unit)
            : this(layout = layout, title = title, data = data,
        selectedIndex = data.index(selected) ?: 0, onSelected = onSelected)

    constructor(@LayoutRes layout: Int, title: CharSequence,
                data: List<Row>, property: CSEventProperty<in Row>)
            : this(layout = layout, title = title, data = data,
        selectedIndex = data.index(property.value) ?: 0, onSelected = { property.value = it })

    constructor(title: CharSequence, data: List<Row>, property: CSEventProperty<in Row>)
            : this(R.layout.cs_item_picker, title, data, property)

    companion object Factory

    val picker = numberPicker(R.id.CS_ItemPicker_NumberPicker)

    init {
        textView(R.id.CS_ItemPicker_TitleTextView).text(title)
        picker.loadData(data, selectedIndex).circulate(false).disableTextEditing(true)
    }

    override fun show() = apply {
        eventOnDismiss.listener { onSelected(data[picker.value - 1]) }
        super.show()
    }

    fun showWithOk() =
        show(R.string.cs_dialog_ok, onPositive = { onSelected(data[picker.value - 1]) })

    fun showWithOkAndCancel() =
        show(onPositive = { onSelected(data[picker.value - 1]) }, onNegative = { hide() })
}




