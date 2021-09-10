package renetik.android.widget

import android.widget.AbsListView
import renetik.kotlin.later

fun AbsListView.scrollToIndex(index: Int, duration: Int = 100) {
    smoothScrollToPositionFromTop(index, 0, duration)
    later(duration) {
        setSelection(index)
        later { smoothScrollToPositionFromTop(index, 0, duration) }
    }
}