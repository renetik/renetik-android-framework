package renetik.android.view.extensions

import android.widget.AbsListView
import renetik.android.java.extensions.later

fun AbsListView.scrollToIndex(index: Int, duration: Int = 100) {
    smoothScrollToPositionFromTop(index, 0, duration)
    later(duration) {
        setSelection(index)
        later { smoothScrollToPositionFromTop(index, 0, duration) }
    }
}