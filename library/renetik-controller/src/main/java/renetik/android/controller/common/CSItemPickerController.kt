package renetik.android.controller.common

import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import renetik.android.base.CSView
import renetik.android.base.layout
import renetik.android.controller.R
import renetik.android.controller.common.CSNavigationInstance.navigation
import renetik.android.extensions.numberPicker
import renetik.android.extensions.textView
import renetik.android.java.common.CSName
import renetik.android.java.event.CSEventProperty
import renetik.android.java.extensions.collections.index
import renetik.android.view.extensions.circulate
import renetik.android.view.extensions.disableTextEditing
import renetik.android.view.extensions.loadData
import renetik.android.view.extensions.title


class CSItemPickerController<Row : CSName>(@LayoutRes layout: Int = R.layout.cs_item_picker,
                                           title: CharSequence, val data: List<Row>,
                                           selectedIndex: Int = 0, val onSelected: (Row) -> Unit)
    : CSView<LinearLayout>(navigation, layout(layout)) {

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

    private lateinit var dialog: AlertDialog

    init {
        textView(R.id.CS_ItemPicker_TitleTextView).title(title)
        picker.loadData(data, selectedIndex).circulate(false).disableTextEditing(true)
    }

    fun show() {
        dialog = MaterialAlertDialogBuilder(this).setView(view).setOnDismissListener {
            onSelected(data[picker.value - 1])
        }.show()
    }

    fun showWithOk() = apply {
        dialog = MaterialAlertDialogBuilder(this).setView(view)
            .setPositiveButton(R.string.cs_dialog_ok) { _, _ -> onSelected(data[picker.value - 1]) }
            .show()
    }

    fun showWithOkAndCancel() = apply {
        dialog = MaterialAlertDialogBuilder(this).setView(view)
            .setPositiveButton(R.string.cs_dialog_ok) { _, _ -> onSelected(data[picker.value - 1]) }
            .setNegativeButton(R.string.cs_dialog_cancel) { _, _ -> dialog.hide() }.show()
    }
}




