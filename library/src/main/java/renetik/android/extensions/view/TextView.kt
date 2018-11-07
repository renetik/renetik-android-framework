package renetik.android.extensions.view

import android.text.Editable
import android.widget.TextView
import renetik.android.extensions.string
import renetik.android.view.adapter.CSTextWatcherAdapter
import renetik.android.lang.CSLang.*


fun <T : TextView> T.title(resourceId: Int): T {
    setText(resourceId)
    return this
}

fun <T : TextView> T.title(string: CharSequence?): T {
    text = string
    return this
}

fun <T : TextView> T.titlef(format: String, vararg arguments: Any): T = title(stringf(format, *arguments))

fun <T : TextView> T.title(): String = string(text)

fun <T : TextView> T.onChange(onChange: (view: T) -> Unit): T {
    val self = this
    addTextChangedListener(object : CSTextWatcherAdapter() {
        override fun afterTextChanged(editable: Editable) {
            onChange(self)
        }
    })
    return this
}

fun <T : TextView> T.hideIfEmpty(): T {
    visible(set(title()))
    return this
}
