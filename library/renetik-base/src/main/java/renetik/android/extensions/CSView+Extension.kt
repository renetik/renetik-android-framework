package renetik.android.extensions

import android.R.layout.simple_spinner_dropdown_item
import android.R.layout.simple_spinner_item
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.*
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import renetik.android.base.CSView
import renetik.android.java.collections.list
import renetik.android.java.common.CSConstants.MINUTE
import renetik.android.view.extensions.*
import java.util.*
import java.util.Calendar.HOUR_OF_DAY
import java.util.Calendar.getInstance

fun <T : View> CSView<*>.findView(id: Int): T? = view.findView<T>(id)
fun CSView<*>.simpleView(id: Int) = view.simpleView(id)
fun CSView<*>.editText(id: Int) = view.editText(id)
fun CSView<*>.textView(id: Int) = view.textView(id)
fun CSView<*>.scrollView(id: Int) = view.scrollView(id)
fun CSView<*>.listView(id: Int) = view.listView(id)
fun CSView<*>.radio(id: Int) = view.radio(id)
fun CSView<*>.radioGroup(id: Int) = view.radioGroup(id)
fun CSView<*>.datePicker(id: Int) = view.datePicker(id)
fun CSView<*>.frame(id: Int) = view.frame(id)
fun CSView<*>.linearLayout(id: Int) = view.linearLayout(id)
fun CSView<*>.viewGroup(id: Int) = view.viewGroup(id)
fun CSView<*>.spinner(id: Int) = view.spinner(id)
fun CSView<*>.button(id: Int) = view.button(id)
fun CSView<*>.compoundButton(id: Int) = view.compoundButton(id)
fun CSView<*>.checkBox(id: Int) = view.checkBox(id)
fun CSView<*>.timePicker(id: Int) = view.timePicker(id)
fun CSView<*>.webView(id: Int) = view.webView(id)
fun CSView<*>.imageView(id: Int) = view.imageView(id)
fun CSView<*>.swipeRefresh(id: Int) = view.swipeRefresh(id)

//move to DatePicker extension
fun CSView<*>.getDate(picker: DatePicker) = getInstance().apply {
    set(picker.year, picker.month, picker.dayOfMonth)
}.time

//move to TimePicker extension
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
    val adapter = ArrayAdapter<String>(this, itemLayout, list<String>(strings))
    adapter.setDropDownViewResource(dropDownItemLayout)
    spinner.adapter = adapter
}

fun CSView<*>.inflateView(layoutId: Int) = inflate<View>(layoutId)


