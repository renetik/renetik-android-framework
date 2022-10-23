package renetik.android.ui.extensions.widget

import android.widget.HorizontalScrollView

val HorizontalScrollView.content get() = getChildAt(0)

fun HorizontalScrollView.setTouchEnabled(enabled: Boolean) {
    if (enabled) setOnTouchListener(null) else setOnTouchListener { _, _ -> true }
}

fun HorizontalScrollView.scrollX(x: Int) = apply { smoothScrollTo(x, scrollY) }

fun HorizontalScrollView.scrollY(y: Int) = apply { smoothScrollTo(scrollY, y) }