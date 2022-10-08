package renetik.android.ui.extensions.widget

import android.widget.HorizontalScrollView

val HorizontalScrollView.content get() = getChildAt(0)

fun HorizontalScrollView.setTouchEnabled(enabled: Boolean) {
    if (enabled) setOnTouchListener(null) else setOnTouchListener { _, _ -> true }
}

fun HorizontalScrollView.smoothScrollX(x: Int) = apply { smoothScrollTo(x, scrollY) }

fun HorizontalScrollView.smoothScrollY(y: Int) = apply { smoothScrollTo(scrollY, y) }