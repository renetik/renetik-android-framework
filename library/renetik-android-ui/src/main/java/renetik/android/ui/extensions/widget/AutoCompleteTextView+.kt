package renetik.android.ui.extensions.widget

import android.R.layout.simple_spinner_dropdown_item
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Filter
import renetik.android.core.kotlin.collections.contains
import renetik.android.core.logging.CSLog.logDebug
import renetik.android.event.registration.CSRegistration

fun AutoCompleteTextView.reset() = apply { setAdapter(null); text(null) }

val AutoCompleteTextView.selectedIndex: Int?
    get() = if (listSelection >= 0) listSelection else null

// simple_dropdown_item_1line
fun AutoCompleteTextView.setDropDown(
    stringArray: Int, selectedIndex: Int? = null,
    disableEdit: Boolean = true, isAutoClear: Boolean = true,
    onItemSelected: ((position: Int?) -> Unit)? = null
) = setDropDown(resources.getStringArray(stringArray).toList(),
    selectedIndex, disableEdit, isAutoClear, onItemSelected)

fun <T : AutoCompleteTextView> T.setDropDown(
    strings: List<String>, filter: ((List<String>, CharSequence?) -> List<String>)?,
    selectedIndex: Int? = null, isAutoClear: Boolean = true,
    onSelection: ((position: Int?) -> Unit)? = null
): CSRegistration {
    val adapter = object : ArrayAdapter<String>(
        context, simple_spinner_dropdown_item, strings.toList()) {
        override fun getFilter(): Filter = filter?.let {
            StringArrayAdapterFilter(this, strings, it)
        } ?: super.filter
    }
    selectedIndex?.let(strings::getOrNull)?.also { setText(it, false) }
    setAdapter(adapter)
    var selectedItem: String? = selectedIndex?.let { adapter.getItem(it) }
    val onFocus = if (isAutoClear) onFocusLost {
        if (selectedItem == null) clearText()
        else if (!strings.contains { it == text() }) {
            selectedItem = null
            onSelection?.invoke(null)
            clearText()
        }
    } else null
    val onTextChange = onTextChange {
        if (text.isBlank()) {
            selectedItem = null
            onSelection?.invoke(null)
        }
    }
    setOnItemClickListener { _, _, position, _ ->
        selectedItem = adapter.getItem(position)!!
        onSelection?.invoke(strings.indexOf(selectedItem))
        logDebug(this)
    }
    isFocusable = true
    isFocusableInTouchMode = true
    return CSRegistration(onFocus, onTextChange)
}

fun <T : AutoCompleteTextView> T.setDropDown(
    strings: List<String>, selectedIndex: Int? = null,
    disableEdit: Boolean = true, isAutoClear: Boolean = true,
    onSelection: ((position: Int?) -> Unit)? = null
): CSRegistration {
    if (disableEdit) keyListener = null //To disable user editing
    return setDropDown(strings,
        filter = if (disableEdit) { _, _ -> strings } else null,
        selectedIndex, isAutoClear, onSelection
    )
}

//fun <T : AutoCompleteTextView> T.setDropDown(
//    strings: List<String>, selectedIndex: Int? = null,
//    disableEdit: Boolean = true, isAutoClear: Boolean = true,
//    onSelection: ((position: Int?) -> Unit)? = null
//): CSRegistration {
//    val adapter = if (disableEdit) object :
//        ArrayAdapter<String>(context, simple_spinner_dropdown_item, strings) {
//        override fun getFilter(): Filter = object : Filter() {
//            override fun performFiltering(constraint: CharSequence?): FilterResults =
//                FilterResults().apply { values = strings; count = strings.size }
//
//            override fun publishResults(constraint: CharSequence?, results: FilterResults?) =
//                notifyDataSetChanged()
//        }
//    } else ArrayAdapter(context, simple_spinner_dropdown_item, strings)
//
//    selectedIndex?.let(strings::getOrNull)?.also { setText(it, false) }
//    setAdapter(adapter)
//    var selectedItem: String? = selectedIndex?.let { adapter.getItem(it) }
//    val onFocus = if (isAutoClear) onFocusLost {
//        if (selectedItem == null) clearText()
//        else if (!strings.contains { it == text() }) {
//            selectedItem = null
//            onSelection?.invoke(null)
//            clearText()
//        }
//    } else null
//    val onTextChange = onTextChange {
//        if (text.isBlank()) {
//            selectedItem = null
//            onSelection?.invoke(null)
//        }
//    }
//    setOnItemClickListener { _, _, position, _ ->
//        selectedItem = adapter.getItem(position)!!
//        onSelection?.invoke(strings.indexOf(selectedItem))
//        logDebug(this)
//    }
//    if (disableEdit) keyListener = null //To disable user editing
//    isFocusable = true
//    isFocusableInTouchMode = true
//    return CSRegistration(onFocus, onTextChange)
//}

class StringArrayAdapterFilter(
    val adapter: ArrayAdapter<String>,
    val strings: List<String>,
    val performFilter: (List<String>, CharSequence?) -> List<String>
) : Filter() {

    override fun performFiltering(constraint: CharSequence?): FilterResults =
        performFilter(strings, constraint).let {
            FilterResults().apply { values = it; count = it.size }
        }

    override fun publishResults(constraint: CharSequence?, results: FilterResults) {
        @Suppress("UNCHECKED_CAST")
        val list = results.values as? List<String> ?: return
        adapter.clear()
        adapter.addAll(list)
        adapter.notifyDataSetChanged()
    }
}