package renetik.android.controller.common

import android.widget.FrameLayout
import android.widget.NumberPicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import renetik.android.base.CSContextController
import renetik.android.controller.R
import renetik.android.java.common.CSName
import renetik.android.java.event.CSEventProperty
import renetik.android.java.extensions.collections.index
import renetik.android.view.extensions.add
import renetik.android.view.extensions.horizontalMarginDp
import renetik.android.view.extensions.layoutMatchCenter
import renetik.android.view.extensions.loadData

class CSMaterialItemPickerController<Row : CSName>(
    title: CharSequence, data: List<Row>, selectedIndex: Int = 0, onSelected: (Row) -> Unit)
    : CSContextController(navigation) {

    constructor(title: CharSequence, data: List<Row>, selected: Row, onSelected: (Row) -> Unit)
            : this(title, data, data.index(selected) ?: 0, onSelected)

    constructor(title: CharSequence, data: List<Row>, property: CSEventProperty<in Row>)
            : this(title, data, data.index(property.value) ?: 0, { property.value = it })

    companion object Factory

    init {
        val layout = FrameLayout(this)
        val picker = NumberPicker(this).loadData(data, selectedIndex)
        layout.add(picker, layoutMatchCenter).horizontalMarginDp(15)
        MaterialAlertDialogBuilder(this).setView(picker).setTitle(title)
            .setPositiveButton(R.string.renetik_android_base_yes) { _, _ ->
                onSelected(data[picker.value - 1])
            }.show()
    }
}


