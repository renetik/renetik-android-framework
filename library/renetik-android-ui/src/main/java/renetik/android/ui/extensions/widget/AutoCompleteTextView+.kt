package renetik.android.ui.extensions.widget

import android.R.layout.simple_spinner_dropdown_item
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView

val AutoCompleteTextView.selectedIndex: Int?
    get() = if (listSelection >= 0) listSelection else null

// simple_dropdown_item_1line
fun AutoCompleteTextView.setDropDown(
    string: Int, onItemSelected: (position: Int) -> Unit) = apply {
    val categories = resources.getStringArray(string)
    val adapter = ArrayAdapter(context, simple_spinner_dropdown_item, categories)
    setAdapter(adapter)
    setOnClickListener { showDropDown() }
    setOnItemClickListener { _, _, position, _ ->
        onItemSelected(position)
    }
    keyListener = null //To disable user editing
}

fun <T:AutoCompleteTextView> T.setDropDown(
    strings: List<String>, onItemSelected: ((position: Int) -> Unit)? = null) = apply {
    val adapter = ArrayAdapter(context, simple_spinner_dropdown_item, strings)
    setAdapter(adapter)
    setOnClickListener { showDropDown() }
    setOnItemClickListener { _, _, position, _ -> onItemSelected?.invoke(position) }
    keyListener = null //To disable user editing
}