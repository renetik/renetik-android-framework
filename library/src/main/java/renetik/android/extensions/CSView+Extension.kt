package renetik.android.extensions

import android.R.layout.simple_spinner_dropdown_item
import android.R.layout.simple_spinner_item
import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.*
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import renetik.android.extensions.view.findView
import renetik.android.extensions.view.isVisible
import renetik.java.collections.CSList
import renetik.java.collections.list
import renetik.android.view.CSDialog
import renetik.android.view.base.CSView
import renetik.java.lang.CSLang
import java.util.*
import java.util.Calendar.HOUR_OF_DAY
import java.util.Calendar.getInstance

fun <T : View> CSView<*>.findView(id: Int): T = view.findView<T>(id)!!
fun CSView<*>.view(id: Int) = findView<View>(id)
fun CSView<*>.editText(id: Int) = findView<EditText>(id)
fun CSView<*>.textView(id: Int) = findView<TextView>(id)
fun CSView<*>.listView(id: Int): ListView = findView(id)
fun CSView<*>.chip(id: Int) = findView<Chip>(id)
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

fun CSView<*>.onClick(listener: OnClickListener) = apply { view.setOnClickListener(listener) }
fun CSView<*>.onLongClick(listener: OnLongClickListener) = apply { view.setOnLongClickListener(listener) }

val CSView<*>.isVisible get() = view.isVisible
val CSView<*>.isShown get() = view.isShown()

fun CSView<*>.getDate(picker: Int) = getDate(datePicker(picker))
fun CSView<*>.getDate(picker: DatePicker) = getInstance().apply {
    set(picker.year, picker.month, picker.dayOfMonth)
}.time

fun CSView<*>.getTime(picker: Int) = getTime(timePicker(picker))
fun CSView<*>.getTime(picker: TimePicker): Date {
    return Calendar.getInstance().apply {
        @Suppress("DEPRECATION") set(HOUR_OF_DAY, picker.currentHour)
        @Suppress("DEPRECATION") set(CSLang.MINUTE, picker.currentMinute)
    }.time
}

fun CSView<*>.initSpinner(id: Int, values: CSList<String>, value: String) {
    setSpinnerData(spinner(id), values)
    spinner(id).setSelection(values.indexOf(value), CSLang.NO)
}

fun CSView<*>.setSpinnerData(spinner: Spinner, strings: Collection<String>) =
        setSpinnerData(spinner, simple_spinner_item, simple_spinner_dropdown_item, strings)

fun CSView<*>.setSpinnerData(spinner: Spinner, itemLayout: Int,
                                                       dropDownItemLayout: Int, strings: Collection<String>) {
    val adapter = ArrayAdapter<String>(this, itemLayout, list<String>(strings))
    adapter.setDropDownViewResource(dropDownItemLayout)
    spinner.adapter = adapter
}

fun CSView<*>.dialog() = CSDialog(this)
fun CSView<*>.dialog(message: String) = dialog().message(message)
fun CSView<*>.dialog(title: String, message: String) = dialog().title(title).message(message)

val CSView<*>.displayMetrics get() = view.resources.displayMetrics
fun CSView<*>.toDp(pixel: Float) = pixel / (displayMetrics.densityDpi / 160f)
fun CSView<*>.toPixelInt(dp: Float) = toPixel(dp).toInt()
fun CSView<*>.toPixel(dp: Float) = dp * (displayMetrics.densityDpi / 160f)

