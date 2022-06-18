package renetik.android.ui.extensions.widget

import android.widget.AbsListView
import renetik.android.core.lang.CSMainHandler.postOnMain

fun AbsListView.scrollToIndex(index: Int, smooth: Boolean = true) {
    if (smooth) {
        smoothScrollToPositionFromTop(index, 0, 100)
        postOnMain(100) {
            setSelection(index)
            postOnMain { smoothScrollToPositionFromTop(index, 0, 100) }
        }
    } else {
        setSelection(index)
        postOnMain { smoothScrollToPositionFromTop(index, 0, 100) }
    }
}