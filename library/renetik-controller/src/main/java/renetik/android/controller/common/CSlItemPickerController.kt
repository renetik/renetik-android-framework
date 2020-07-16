package renetik.android.controller.common

import android.app.AlertDialog
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import renetik.android.base.CSView
import renetik.android.base.layout
import renetik.android.controller.R
import renetik.android.extensions.numberPicker
import renetik.android.extensions.textView
import renetik.android.java.common.CSName
import renetik.android.java.event.CSEventProperty
import renetik.android.java.extensions.collections.index
import renetik.android.view.extensions.circulate
import renetik.android.view.extensions.loadData
import renetik.android.view.extensions.title


class CSlItemPickerController<Row : CSName>(@LayoutRes layout: Int = R.layout.cs_item_picker,
                                            title: CharSequence, data: List<Row>,
                                            selectedIndex: Int = 0, onSelected: (Row) -> Unit)
    : CSView<LinearLayout>(navigation, layout(layout)) {

    constructor(@LayoutRes layout: Int = R.layout.cs_item_picker, title: CharSequence,
                data: List<Row>, selected: Row, onSelected: (Row) -> Unit)
            : this(layout = layout, title = title, data = data,
        selectedIndex = data.index(selected) ?: 0, onSelected = onSelected)

    constructor(@LayoutRes layout: Int = R.layout.cs_item_picker, title: CharSequence,
                data: List<Row>, property: CSEventProperty<in Row>)
            : this(layout = layout, title = title, data = data,
        selectedIndex = data.index(property.value) ?: 0, onSelected = { property.value = it })

    companion object Factory

    val picker = numberPicker(R.id.CS_ItemPicker_NumberPicker)

    init {
        textView(R.id.CS_ItemPicker_TitleTextView).title(title)
        picker.loadData(data, selectedIndex).circulate(false)
        AlertDialog.Builder(this).setView(view).show().setOnDismissListener {
            onSelected(data[picker.value - 1])
        }
    }
}


