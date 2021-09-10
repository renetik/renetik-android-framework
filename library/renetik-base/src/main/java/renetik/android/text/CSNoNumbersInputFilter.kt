package renetik.android.text

import android.text.InputFilter
import android.text.Spanned

class CSNoNumbersInputFilter : InputFilter {
    override fun filter(source: CharSequence, start: Int, end: Int,
                        dest: Spanned, dstart: Int, dend: Int): CharSequence {
        for (index in start until end)
            if (!Character.isLetter(source[index])) return ""
        return source
    }
}