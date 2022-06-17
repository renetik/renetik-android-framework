package renetik.android.widget

import android.widget.AutoCompleteTextView

val AutoCompleteTextView.selectedIndex: Int?
    get() = if (listSelection >= 0) listSelection else null