package renetik.android.text

import android.text.InputFilter
import android.text.Spanned

class CSIntMaxInputFilter(val getMaxValue: () -> Int) : InputFilter {
    override fun filter(source: CharSequence, start: Int, end: Int,
                        dest: Spanned, dstart: Int, dend: Int): CharSequence {
        val resultString = StringBuilder(dest).insert(dstart, source).toString()
        return if (resultString.toInt() > getMaxValue()) "" else source
    }
}