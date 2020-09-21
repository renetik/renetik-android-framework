package renetik.android.view.extensions

import android.content.res.ColorStateList
import android.widget.TextView
import androidx.annotation.ColorInt

fun TextView.textColor(@ColorInt value: Int) = apply {
    setTextColor(ColorStateList.valueOf(value))
}