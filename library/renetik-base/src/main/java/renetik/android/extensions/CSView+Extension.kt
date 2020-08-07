package renetik.android.extensions

import android.R.layout.simple_spinner_dropdown_item
import android.R.layout.simple_spinner_item
import android.view.View
import android.widget.*
import androidx.annotation.IdRes
import renetik.android.base.CSView
import renetik.android.java.extensions.collections.list
import renetik.android.view.extensions.*
import java.util.*
import java.util.Calendar.*

fun <T : View> CSView<*>.findView(@IdRes id: Int): T? = view.findView(id)
fun CSView<*>.view(@IdRes id: Int, onClick: ((view: View) -> Unit)? = null) =
    view.view(id).apply { onClick?.let { this.onClick(it) } }

fun CSView<*>.simpleView(@IdRes id: Int) = view.view(id)
fun CSView<*>.editText(@IdRes id: Int) = view.editText(id)
fun CSView<*>.textView(@IdRes id: Int) = view.textView(id)
fun CSView<*>.scrollView(@IdRes id: Int) = view.scrollView(id)
fun CSView<*>.horizontalScroll(@IdRes id: Int) = view.horizontalScroll(id)
fun CSView<*>.listView(@IdRes id: Int) = view.listView(id)
fun CSView<*>.radio(@IdRes id: Int) = view.radio(id)
fun CSView<*>.radioGroup(@IdRes id: Int) = view.radioGroup(id)
fun CSView<*>.datePicker(@IdRes id: Int) = view.datePicker(id)
fun CSView<*>.numberPicker(@IdRes id: Int) = view.numberPicker(id)
fun CSView<*>.frame(@IdRes id: Int) = view.frame(id)
fun CSView<*>.linearLayout(@IdRes id: Int) = view.linearLayout(id)
fun CSView<*>.viewGroup(@IdRes id: Int) = view.viewGroup(id)
fun CSView<*>.spinner(@IdRes id: Int) = view.spinner(id)
fun CSView<*>.search(@IdRes id: Int) = view.search(id)
fun CSView<*>.button(@IdRes id: Int, onClick: ((view: Button) -> Unit)? = null) =
    view.button(id).apply { onClick?.let { this.onClick(it) } }

fun CSView<*>.compound(@IdRes id: Int) = view.compound(id)
fun CSView<*>.switch(@IdRes id: Int) = view.switch(id)
fun CSView<*>.checkBox(@IdRes id: Int) = view.checkBox(id)
fun CSView<*>.timePicker(@IdRes id: Int) = view.timePicker(id)
fun CSView<*>.webView(@IdRes id: Int) = view.webView(id)
fun CSView<*>.imageView(@IdRes id: Int, onClick: ((view: ImageView) -> Unit)? = null) =
    view.imageView(id).apply { onClick?.let { this.onClick(it) } }

fun CSView<*>.swipeRefresh(@IdRes id: Int) = view.swipeRefresh(id)
fun CSView<*>.seekBar(@IdRes id: Int) = view.seekBar(id)

//move to DatePicker extension //TODO move to DatePicker extension
fun CSView<*>.getDate(picker: DatePicker): Date = getInstance().apply {
    set(picker.year, picker.month, picker.dayOfMonth)
}.time

//move to TimePicker extension //TODO move to TimePicker extension
fun CSView<*>.getTime(picker: TimePicker): Date {
    return Calendar.getInstance().apply {
        @Suppress("DEPRECATION") set(HOUR_OF_DAY, picker.currentHour)
        @Suppress("DEPRECATION") set(MINUTE, picker.currentMinute)
    }.time
}


//move to Spinner extension
fun CSView<*>.initSpinner(id: Int, values: List<String>, value: String) {
    setSpinnerData(spinner(id), values)
    spinner(id).setSelection(values.indexOf(value), false)
}

fun CSView<*>.setSpinnerData(spinner: Spinner, strings: Collection<String>) =
    setSpinnerData(spinner, simple_spinner_item, simple_spinner_dropdown_item, strings)

fun CSView<*>.setSpinnerData(spinner: Spinner, itemLayout: Int,
                             dropDownItemLayout: Int, strings: Collection<String>) {
    val adapter = ArrayAdapter<String>(this, itemLayout, list(strings))
    adapter.setDropDownViewResource(dropDownItemLayout)
    spinner.adapter = adapter
}

fun CSView<*>.inflateView(layoutId: Int) = inflate<View>(layoutId)


