package renetik.android.ui.extensions.widget

import android.R.layout.simple_spinner_dropdown_item
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView

fun AutoCompleteTextView.reset() = apply { setAdapter(null); text("") }

val AutoCompleteTextView.selectedIndex: Int?
    get() = if (listSelection >= 0) listSelection else null

// simple_dropdown_item_1line
fun AutoCompleteTextView.setDropDown(
    string: Int, disableEdit: Boolean = true,
    onItemSelected: ((position: Int) -> Unit)? = null) = apply {
    val strings = resources.getStringArray(string)
    val adapter = ArrayAdapter(context, simple_spinner_dropdown_item, strings)
    setAdapter(adapter)
    setOnClickListener { showDropDown() }
    setOnItemClickListener { _, _, position, _ ->
        val selectedItem = adapter.getItem(position) as String
        onItemSelected?.invoke(strings.indexOf(selectedItem))
    }
    if (disableEdit) keyListener = null //To disable user editing
}

fun <T : AutoCompleteTextView> T.setDropDown(
    strings: List<String>, disableEdit: Boolean = true,
    onItemSelected: ((position: Int) -> Unit)? = null) = apply {
    val adapter = ArrayAdapter(context, simple_spinner_dropdown_item, strings)
    setAdapter(adapter)
    setOnClickListener { showDropDown() }
    setOnItemClickListener { _, _, position, _ ->
        val selectedItem = adapter.getItem(position) as String
        onItemSelected?.invoke(strings.indexOf(selectedItem))
    }
    if (disableEdit) keyListener = null
}