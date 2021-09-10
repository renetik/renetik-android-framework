package renetik.android.widget

import android.R.layout.simple_spinner_dropdown_item
import android.R.layout.simple_spinner_item
import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Spinner
import renetik.kotlin.collections.list

fun <Data> Spinner.data(context: Context, values: Collection<Data>, selected: Data? = null) =
    data(context, simple_spinner_item, simple_spinner_dropdown_item, values, selected)

fun <Data> Spinner.data(context: Context, itemLayout: Int, dropDownItemLayout: Int,
                        values: Collection<Data>, selected: Data? = null) {
    adapter = ArrayAdapter(context, itemLayout, list(values)).apply {
        setDropDownViewResource(dropDownItemLayout)
    }
    selected?.let { setSelection(values.indexOf(it), false) }
}