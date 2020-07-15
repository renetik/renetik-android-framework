package renetik.android.controller.common

import android.app.AlertDialog
import android.os.Build
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import renetik.android.base.CSView
import renetik.android.base.layout
import renetik.android.controller.R
import renetik.android.extensions.numberPicker
import renetik.android.extensions.textView
import renetik.android.extensions.toPixel
import renetik.android.java.common.CSName
import renetik.android.java.event.CSEventProperty
import renetik.android.java.extensions.collections.index
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

    init {
        textView(R.id.CS_ItemPicker_TitleTextView).title(title)
        numberPicker(R.id.CS_ItemPicker_NumberPicker).loadData(data, selectedIndex)
            .setOnValueChangedListener { _, _, newVal -> onSelected(data[newVal - 1]) }
        numberPicker(R.id.CS_ItemPicker_NumberPicker).wrapSelectorWheel = true
        AlertDialog.Builder(this).setView(view).show()
    }
}


