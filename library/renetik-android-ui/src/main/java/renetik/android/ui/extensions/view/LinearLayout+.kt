package renetik.android.ui.extensions.view

import android.view.View
import android.widget.LinearLayout

fun LinearLayout.reverseChildren() {
    val children: List<View> = (0 until childCount).map { getChildAt(it) }
    removeAllViews()
    children.asReversed().forEach { addView(it) }
}