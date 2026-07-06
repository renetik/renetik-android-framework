package renetik.android.ui.view.adapter

import android.text.Editable
import android.text.TextWatcher

open class CSTextWatcherAdapter : TextWatcher {
    override fun beforeTextChanged(sequence: CharSequence, start: Int, count: Int, after: Int) = Unit
    override fun onTextChanged(sequence: CharSequence, start: Int, before: Int, count: Int) = Unit
    override fun afterTextChanged(editable: Editable) = Unit
}
