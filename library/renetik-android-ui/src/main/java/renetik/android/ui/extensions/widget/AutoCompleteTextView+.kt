package renetik.android.ui.extensions.widget

import android.R.layout.simple_spinner_dropdown_item
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Filter

fun AutoCompleteTextView.reset() = apply { setAdapter(null); text("") }

val AutoCompleteTextView.selectedIndex: Int?
    get() = if (listSelection >= 0) listSelection else null

// simple_dropdown_item_1line
fun AutoCompleteTextView.setDropDown(
    string: Int, disableEdit: Boolean = true, selectedIndex: Int? = null,
    onItemSelected: ((position: Int) -> Unit)? = null) = apply {
    val strings = resources.getStringArray(string)
    val adapter = object : ArrayAdapter<String>(context, simple_spinner_dropdown_item, strings) {
        override fun getFilter(): Filter = object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults =
                FilterResults().apply { values = strings; count = strings.size }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) =
                notifyDataSetChanged()
        }
    }
    selectedIndex?.let(strings::getOrNull)?.also { setText(it, false) }
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