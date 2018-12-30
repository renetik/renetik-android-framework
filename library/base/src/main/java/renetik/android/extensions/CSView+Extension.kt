package renetik.android.extensions

import android.R.layout.simple_spinner_dropdown_item
import android.R.layout.simple_spinner_item
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.*
import renetik.android.base.CSView
import renetik.android.java.collections.list
import renetik.android.java.common.CSConstants.MINUTE
import renetik.android.view.extensions.findView
import renetik.android.view.extensions.title
import java.util.*
import java.util.Calendar.HOUR_OF_DAY
import java.util.Calendar.getInstance

fun <T : View> CSView<*>.findView(id: Int): T? = view.findView<T>(id)
fun CSView<*>.simpleView(id: Int) = findView<View>(id)!!
fun CSView<*>.editText(id: Int) = findView<EditText>(id)!!
fun CSView<*>.textView(id: Int) = findView<TextView>(id)!!
fun CSView<*>.listView(id: Int) = findView<ListView>(id)!!
fun CSView<*>.radio(id: Int) = findView<RadioButton>(id)!!
fun CSView<*>.radioGroup(id: Int) = findView<RadioGroup>(id)!!
fun CSView<*>.datePicker(id: Int) = findView<DatePicker>(id)!!
fun CSView<*>.frame(id: Int) = findView<FrameLayout>(id)!!
fun CSView<*>.linearLayout(id: Int) = findView<LinearLayout>(id)!!
fun CSView<*>.viewGroup(id: Int) = findView<ViewGroup>(id)!!
fun CSView<*>.spinner(id: Int) = findView<Spinner>(id)!!
fun CSView<*>.button(id: Int) = findView<Button>(id)!!
fun CSView<*>.compoundButton(id: Int) = findView<CompoundButton>(id)!!
fun CSView<*>.checkBox(id: Int) = findView<CheckBox>(id)!!
fun CSView<*>.timePicker(id: Int) = findView<TimePicker>(id)!!
fun CSView<*>.webView(id: Int) = findView<WebView>(id)!!
fun CSView<*>.imageView(id: Int) = findView<ImageView>(id)!!

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


fun CSView<*>.title(id: Int, value: String) {
    textView(id).title(value)
}


