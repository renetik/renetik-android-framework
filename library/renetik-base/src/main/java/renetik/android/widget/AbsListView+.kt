package renetik.android.widget

import android.widget.AbsListView
import renetik.kotlin.later

fun AbsListView.scrollToIndex(index: Int) {
    smoothScrollToPositionFromTop(index, 0, 100)
    later(100) {
        setSelection(index)
        later { smoothScrollToPositionFromTop(index, 0, 100) }
    }
}