package renetik.android.view.extensions

import android.content.res.ColorStateList
import android.widget.TextView
import renetik.android.extensions.color

fun TextView.textColor(value: Int) = apply {
    setTextColor(ColorStateList.valueOf(context.color(value)))
}