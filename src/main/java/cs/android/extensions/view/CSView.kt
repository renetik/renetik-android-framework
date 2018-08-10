package cs.android.extensions.view

import android.text.Editable
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.*
import cs.android.view.adapter.CSTextWatcherAdapter
import cs.android.viewbase.CSLayoutId
import cs.android.viewbase.CSView
import cs.java.collections.CSList
import cs.java.lang.CSLang.NO
import cs.java.lang.CSLang.list
import java.util.*
import java.util.Calendar.*

fun CSView<*>.view(id: Int): View = item<View>(id).view
fun CSView<*>.editText(id: Int): EditText = item<EditText>(id).view
fun CSView<*>.textView(id: Int): TextView = item<TextView>(id).view
fun CSView<*>.datePicker(id: Int): DatePicker = item<DatePicker>(id).view
fun CSView<*>.frame(id: Int): FrameLayout = item<FrameLayout>(id).view
fun CSView<*>.linearLayout(id: Int): LinearLayout = item<LinearLayout>(id).view
fun CSView<*>.viewGroup(id: Int): ViewGroup = item<ViewGroup>(id).view
fun CSView<*>.spinner(id: Int): Spinner = item<Spinner>(id).view
fun CSView<*>.compoundButton(id: Int): CompoundButton = item<CompoundButton>(id).view
fun CSView<*>.button(id: Int): Button = item<Button>(id).view
fun CSView<*>.timePicker(id: Int): TimePicker = item<TimePicker>(id).view
fun CSView<*>.webView(id: Int): WebView = item<WebView>(id).view

fun CSView<*>.visible(): Boolean = isVisible(view)

fun CSView<*>.shown(): Boolean = isShown(view)

fun <V : View> CSView<V>.onTextChange(onChange: (text: String) -> Unit): CSView<V> {
    asTextView()?.addTextChangedListener(object : CSTextWatcherAdapter() {
        override fun afterTextChanged(editable: Editable) {
            onChange(editable.toString())
        }
    })
    return this
}

fun <V : View> CSView<V>.onChange(onChange: (view: CSView<V>) -> Unit): CSView<V> {
    val self = this
    asTextView()?.addTextChangedListener(object : CSTextWatcherAdapter() {
        override fun afterTextChanged(editable: Editable) {
            onChange(self)
        }
    })
    return this
}

fun CSView<*>.getDate(picker: Int): Date {
    return getDate(datePicker(picker))
}

fun CSView<*>.getDate(picker: DatePicker): Date {
    return getInstance().apply { set(picker.year, picker.month, picker.dayOfMonth) }.time
}

fun CSView<*>.getTime(picker: Int): Date {
    return getTime(timePicker(picker))
}

fun CSView<*>.getTime(picker: TimePicker): Date {
    return Calendar.getInstance().apply {
        set(HOUR_OF_DAY, picker.currentHour)
        set(MINUTE, picker.currentMinute)
    }.time
}

fun CSView<*>.initSpinner(id: Int, values: CSList<String>, value: String) {
    setSpinnerData(spinner(id), values)
    spinner(id).setSelection(values.index(value), NO)
}

fun CSView<*>.setSpinnerData(spinner: Spinner, strings: Collection<String>) {
    setSpinnerData(spinner, android.R.layout.simple_spinner_item, android.R.layout.simple_spinner_dropdown_item, strings)
}

fun CSView<*>.setSpinnerData(spinner: Spinner, itemLayout: Int, dropDownItemLayout: Int, strings: Collection<String>) {
    val adapter = ArrayAdapter<String>(context(), itemLayout, list<String>(strings))
    adapter.setDropDownViewResource(dropDownItemLayout)
    spinner.adapter = adapter
}

fun <V : View> CSView<V>.viewAsChildOf(parentViewGroup: Int, layout: CSLayoutId): CSView<V> =
        viewAsChildOf(viewGroup(parentViewGroup), layout)

fun <V : View> CSView<V>.viewAsChildOf(parent: ViewGroup, layout: CSLayoutId): CSView<V> {
    return CSView<V>(parent, layout).apply { parent.addView(view) }
}





