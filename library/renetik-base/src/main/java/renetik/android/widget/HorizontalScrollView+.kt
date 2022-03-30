package renetik.android.widget

import android.widget.HorizontalScrollView

val HorizontalScrollView.content get() = getChildAt(0)

fun HorizontalScrollView.setTouchEnabled(enabled: Boolean) {
    if (enabled) setOnTouchListener(null) else setOnTouchListener { _, _ -> true }
}