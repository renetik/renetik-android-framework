package cs.android.extensions.view

import android.text.Editable
import android.widget.TextView
import cs.android.view.adapter.CSTextWatcherAdapter
import cs.java.lang.CSLang.*


fun <T : TextView> T.title(resourceId: Int): T {
    setText(resourceId)
    return this
}

fun <T : TextView> T.title(string: CharSequence?): T {
    text = string
    return this
}

fun <T : TextView> T.titlef(format: String, vararg arguments: Any): T = title(stringf(format, *arguments))

fun <T : TextView> T.title(): String = stringify(text)

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
