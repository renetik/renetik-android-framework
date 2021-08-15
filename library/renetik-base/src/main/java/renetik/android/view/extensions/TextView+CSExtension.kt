package renetik.android.view.extensions

import android.text.Editable
import android.widget.TextView
import androidx.annotation.StringRes
import renetik.android.content.drawable
import renetik.android.framework.event.*
import renetik.android.framework.event.property.CSEventProperty
import renetik.android.framework.lang.CSDrawableInterface
import renetik.android.java.extensions.asString
import renetik.android.primitives.isSet
import renetik.android.view.adapter.CSTextWatcherAdapter

fun <T : TextView> T.text(@StringRes resourceId: Int) = apply { setText(resourceId) }
fun <T : TextView> T.textPrepend(string: CharSequence?) = text("$string$title")
fun <T : TextView> T.textAppend(string: CharSequence?) = text("$title$string")
fun <T : TextView> T.text(property: CSEventProperty<*>) = text(property.value.asString)
fun <T : TextView> T.text(value: Any?) = text(value.asString)
fun <T : TextView> T.text(string: CharSequence?) = apply { text = string }
fun <T : TextView> T.text() = text.asString

var <T : TextView> T.title: String
    get() = text()
    set(value) {
        text(value)
    }

fun <T : TextView> T.hideIfEmpty() = apply { shownIf(text().isSet) }

fun <T : TextView> T.onTextChange(onChange: (view: T) -> Unit) = apply {
    addTextChangedListener(object : CSTextWatcherAdapter() {
        override fun afterTextChanged(editable: Editable) = onChange(this@onTextChange)
    })
}

fun <T : TextView> T.onFocusChange(onChange: (view: T) -> Unit) = apply {
    setOnFocusChangeListener { _, _ -> onChange(this) }
}

fun TextView.text(
    parent: CSVisibleEventOwner, property: CSEventProperty<*>
) = text(parent, property) { it.asString }

@JvmName("TextViewTextStringProperty")
fun TextView.text(
    parent: CSVisibleEventOwner, property: CSEventProperty<String>
) = text(parent, property) { it }

fun <T> TextView.text(
    parent: CSVisibleEventOwner, property: CSEventProperty<T>,
    valueToString: (T) -> CharSequence
) = apply {
    fun updateText() = text(valueToString(property.value))
    parent.whileShowing(property.onChange { updateText() })
    updateText()
}

//fun <T> TextView.text(
//    property: CSEventProperty<T>, valueToString: (T) -> String
//) = apply {
//    fun updateText() = text(valueToString(property.value))
//    property.onChange { updateText() }
//    updateText()
//}

//fun <T> TextView.text(property: CSEventProperty<T>) = text(property) { it.asString }

fun <T : CSDrawableInterface> TextView.startDrawable(
    parent: CSVisibleEventOwner, property: CSEventProperty<T>) = apply {
    fun updateDrawable() = startDrawable(context.drawable(property.value.drawable))
    parent.whileShowing(property.onChange { updateDrawable() })
    updateDrawable()
}