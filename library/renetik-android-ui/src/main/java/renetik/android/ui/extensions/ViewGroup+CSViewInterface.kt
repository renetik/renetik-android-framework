package renetik.android.ui.extensions

import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.FrameLayout
import androidx.core.view.contains
import renetik.android.ui.extensions.view.add
import renetik.android.ui.extensions.view.clear
import renetik.android.ui.extensions.view.remove
import renetik.android.ui.protocol.CSViewInterface

fun <View : CSViewInterface> ViewGroup.add(view: View): View {
    add(view.view)
    return view
}

fun <View : CSViewInterface> ViewGroup.add(view: View, index: Int = -1): View {
    add(view.view, index)
    return view
}

fun <View : CSViewInterface> ViewGroup.add(
    view: View, params: LayoutParams, index: Int = -1): View {
    add(view.view, params, index)
    return view
}

fun ViewGroup.contains(view: CSViewInterface): Boolean = contains(view.view)

fun <View : CSViewInterface> ViewGroup.remove(view: View): View =
    view.also { remove(it.view) }

