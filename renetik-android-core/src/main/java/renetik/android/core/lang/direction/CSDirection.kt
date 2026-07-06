package renetik.android.core.lang.direction

import renetik.android.core.lang.CSHasTitle

sealed interface CSDirection : CSHasTitle {
    val isHorizontal: Boolean

    companion object {
        val entries: List<CSDirection> =
            CSHorizontalDirection.entries + CSVerticalDirection.entries
    }
}