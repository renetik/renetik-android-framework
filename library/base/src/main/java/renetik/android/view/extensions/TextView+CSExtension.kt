package renetik.android.view.extensions

import android.text.Editable
import android.widget.TextView
import renetik.android.java.extensions.set
import renetik.android.java.extensions.string
import renetik.android.view.adapter.CSTextWatcherAdapter


fun <T : TextView> T.title(resourceId: Int) = apply { setText(resourceId) }
fun <T : TextView> T.title(string: CharSequence?) = apply { text = string }
fun <T : TextView> T.title(): String = string(text)
val <T : TextView> T.title get() = title()

fun <T : TextView> T.hideIfEmpty() = apply { visible(set(title())) }

fun <T : TextView> T.onChange(onChange: (view: T) -> Unit) = apply {
    addTextChangedListener(object : CSTextWatcherAdapter() {
        override fun afterTextChanged(editable: Editable) = onChange(this@onChange)
    })
}

