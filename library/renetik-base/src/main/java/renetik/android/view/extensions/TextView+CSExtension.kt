package renetik.android.view.extensions

import android.text.Editable
import android.widget.TextView
import renetik.android.java.extensions.isSet
import renetik.android.java.extensions.stringify
import renetik.android.view.adapter.CSTextWatcherAdapter


fun <T : TextView> T.title(resourceId: Int) = apply { setText(resourceId) }
fun <T : TextView> T.title(string: CharSequence?) = apply { text = string }
fun <T : TextView> T.text(string: CharSequence?) = title(string)
fun <T : TextView> T.title(): String = text.stringify()
var <T : TextView> T.title: String
    get() = title()
    set(value) {
        title(value)
    }

fun <T : TextView> T.text() = title()

fun <T : TextView> T.hideIfEmpty() = apply { shown(title().isSet) }

fun <T : TextView> T.onChange(onChange: (view: T) -> Unit) = apply {
    addTextChangedListener(object : CSTextWatcherAdapter() {
        override fun afterTextChanged(editable: Editable) = onChange(this@onChange)
    })
}

