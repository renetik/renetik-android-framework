package cs.android.extensions.view

import android.view.ViewGroup
import cs.android.viewbase.CSView

fun CSView<*>.layoutMatchParent(): ViewGroup.LayoutParams {
    return ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
}