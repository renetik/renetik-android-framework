package renetik.android.text

import android.text.InputFilter
import android.text.Spanned

class CSMaxCharactersInputFilter(val maxCharacters: Int) : InputFilter {
    override fun filter(source: CharSequence, start: Int, end: Int,
                        dest: Spanned, dstart: Int, dend: Int): CharSequence {
        val resultString = StringBuilder(dest).insert(dstart, source)
        return if (resultString.length > maxCharacters) "" else source
    }
}