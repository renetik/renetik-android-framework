package renetik.android.extensions.view

import android.text.Editable
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.*
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import renetik.android.view.CSDialog
import renetik.android.view.adapter.CSTextWatcherAdapter
import renetik.android.viewbase.CSLayoutId
import renetik.android.viewbase.CSView
import renetik.android.java.collections.CSList
import renetik.android.lang.CSLang.NO
import renetik.android.lang.CSLang.list
import java.util.*
import java.util.Calendar.*

fun CSView<*>.view(id: Int): View = findView<View>(id)
fun CSView<*>.editText(id: Int): EditText = findView(id)
fun CSView<*>.textView(id: Int): TextView = findView(id)
fun CSView<*>.listView(id: Int): ListView = findView(id)
fun CSView<*>.chip(id: Int): Chip = findView(id)
fun CSView<*>.chipGroup(id: Int): ChipGroup = findView(id)
fun CSView<*>.radio(id: Int): RadioButton = findView(id)
fun CSView<*>.radioGroup(id: Int): RadioGroup = findView(id)
fun CSView<*>.datePicker(id: Int): DatePicker = findView(id)
fun CSView<*>.frame(id: Int): FrameLayout = findView(id)
fun CSView<*>.linearLayout(id: Int): LinearLayout = findView(id)
fun CSView<*>.viewGroup(id: Int): ViewGroup = findView(id)
fun CSView<*>.spinner(id: Int): Spinner = findView(id)
fun CSView<*>.button(id: Int): Button = findView(id)
fun CSView<*>.compoundButton(id: Int): CompoundButton = findView(id)
fun CSView<*>.floatingButton(id: Int): FloatingActionButton = findView(id)
fun CSView<*>.timePicker(id: Int): TimePicker = findView(id)
fun CSView<*>.webView(id: Int): WebView = findView(id)
fun CSView<*>.imageView(id: Int): ImageView = findView(id)

fun CSView<*>.visible(): Boolean = isVisible(view)

fun CSView<*>.shown(): Boolean = isShown(view)

fun <T : CSView<*>> T.onTextChange(onChange: (text: String) -> Unit): T {
    asTextView()?.addTextChangedListener(object : CSTextWatcherAdapter() {
        override fun afterTextChanged(editable: Editable) {
            onChange(editable.toString())
        }
    })
    return this
}

fun <T : CSView<*>> T.onChange(onChange: (view: T) -> Unit): T {
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

@Suppress("DEPRECATION")
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

fun CSView<*>.dialog() = CSDialog(context())

fun CSView<*>.dialog(message: String) = dialog().message(message)

fun CSView<*>.dialog(title: String, message: String) = dialog().title(title).message(message)





