package renetik.android.extensions.view

import android.text.Editable
import android.widget.TextView
import renetik.android.extensions.string
import renetik.android.lang.CSLang.set
import renetik.android.lang.CSLang.stringf
import renetik.android.view.adapter.CSTextWatcherAdapter


fun <T : TextView> T.title(resourceId: Int) = apply { setText(resourceId) }
fun <T : TextView> T.title(string: CharSequence?) = apply { text = string }
fun <T : TextView> T.titlef(format: String, vararg arguments: Any): T = title(stringf(format, *arguments))

fun <T : TextView> T.title(): String = string(text)

fun <T : TextView> T.hideIfEmpty() = apply { visible(set(title())) }

fun <T : TextView> T.onChange(onChange: (view: T) -> Unit) = apply {
    addTextChangedListener(object : CSTextWatcherAdapter() {
        override fun afterTextChanged(editable: Editable) = onChange(this@onChange)
    })
}

