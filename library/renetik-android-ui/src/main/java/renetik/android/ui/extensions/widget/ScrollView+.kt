package renetik.android.ui.extensions.widget

import android.view.View
import android.widget.ScrollView
import renetik.android.ui.extensions.view.locationInWindow
import renetik.android.ui.protocol.CSViewInterface

fun ScrollView.scrollToChild(
    child: View, centered: Boolean = true, smooth: Boolean = false) {
    val toCenterOffset = if (centered) height / 2 - child.height / 2 else 0
    scrollY(child.y.toInt() - toCenterOffset, smooth)
}

fun ScrollView.scrollTo(
    item: CSViewInterface, centered: Boolean = true, smooth: Boolean = false) {
    val viewY = item.view.locationInWindow.y - locationInWindow.y
    val toCenterOffset = if (centered) height / 2 - item.view.height / 2 else 0
    scrollY(viewY - toCenterOffset, smooth)
}

fun ScrollView.scrollX(x: Int, smooth: Boolean = false) = apply {
    if (smooth) smoothScrollTo(x, scrollY) else scrollX = x
}

fun ScrollView.scrollY(y: Int, smooth: Boolean = false) = apply {
    if (smooth) smoothScrollTo(scrollY, y) else scrollY = y
}