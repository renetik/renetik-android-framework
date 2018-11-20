package renetik.android.extensions.view

import android.view.View
import android.view.ViewGroup
import renetik.android.view.base.CSView

fun <T : ViewGroup> T.add(view: CSView<*>) = add(view.view)

fun <T : ViewGroup> T.add(view: View) = apply {
    view.removeFromSuperview()
    addView(view)
}

fun <T : ViewGroup> T.remove(view: CSView<*>) = remove(view.view)

fun <T : ViewGroup> T.remove(view: View) = apply {
    view.removeFromSuperview()
    addView(view)
}