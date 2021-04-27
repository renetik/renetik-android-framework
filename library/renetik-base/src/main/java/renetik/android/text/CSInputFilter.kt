package renetik.android.text

import android.text.InputFilter
import android.text.Spanned
import renetik.android.primitives.asInt

class CSIntMaxValueInputFilter(val getMaxValue: () -> Int) : InputFilter {
    override fun filter(source: CharSequence, start: Int, end: Int,
                        dest: Spanned, dstart: Int, dend: Int): CharSequence {
        val resultString = StringBuilder(dest).insert(dstart, source).toString()
        return if (resultString.asInt() ?: 0 > getMaxValue()) "" else source
    }
}

class CSIntMinValueInputFilter(val getMinValue: () -> Int) : InputFilter {
    override fun filter(source: CharSequence, start: Int, end: Int,
                        dest: Spanned, dstart: Int, dend: Int): CharSequence {
        val resultString = StringBuilder(dest).insert(dstart, source).toString()
        return if (resultString.asInt() ?: 0 < getMinValue()) "" else source
    }
}

class CSMaxCharactersInputFilter(val maxCharacters: Int) : InputFilter {
    override fun filter(source: CharSequence, start: Int, end: Int,
                        dest: Spanned, dstart: Int, dend: Int): CharSequence {
        val resultString = StringBuilder(dest).insert(dstart, source)
        return if (resultString.length > maxCharacters) "" else source
    }
}

class CSNoNumbersInputFilter : InputFilter {
    override fun filter(source: CharSequence, start: Int, end: Int,
                        dest: Spanned, dstart: Int, dend: Int): CharSequence {
        for (index in start until end)
            if (!Character.isLetter(source[index])) return ""
        return source
    }
}