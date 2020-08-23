package renetik.android.view.extensions

import android.text.Editable
import android.widget.TextView
import renetik.android.java.event.CSEventProperty
import renetik.android.java.extensions.asString
import renetik.android.java.extensions.isSet
import renetik.android.view.adapter.CSTextWatcherAdapter


fun <T : TextView> T.title(resourceId: Int) = apply { setText(resourceId) }
fun <T : TextView> T.title(string: CharSequence?) = apply { text = string }
fun <T : TextView> T.title(property: CSEventProperty<*>) = apply {
    text = property.value.asString()
}

fun <T : TextView> T.title(value: Any?) = apply { text = value.asString() }
fun <T : TextView> T.text(string: CharSequence?) = title(string)
fun <T : TextView> T.title() = text.asString()

var <T : TextView> T.title: String
    get() = title()
    set(value) {
        title(value)
    }

fun <T : TextView> T.text() = title()
fun <T : TextView> T.hideIfEmpty() = apply { shownIf(title().isSet) }
fun <T : TextView> T.onTextChange(onChange: (view: T) -> Unit) = apply {
    addTextChangedListener(object : CSTextWatcherAdapter() {
        override fun afterTextChanged(editable: Editable) = onChange(this@onTextChange)
    })
}