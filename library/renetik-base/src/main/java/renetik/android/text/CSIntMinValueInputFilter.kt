package renetik.android.text

import android.text.InputFilter
import android.text.Spanned
import renetik.android.core.kotlin.primitives.asInt

class CSIntMinValueInputFilter(val getMinValue: () -> Int) : InputFilter {
    override fun filter(source: CharSequence, start: Int, end: Int,
                        dest: Spanned, dstart: Int, dend: Int): CharSequence {
        val resultString = StringBuilder(dest).insert(dstart, source).toString()
        return if (resultString.asInt() ?: 0 < getMinValue()) "" else source
    }
}