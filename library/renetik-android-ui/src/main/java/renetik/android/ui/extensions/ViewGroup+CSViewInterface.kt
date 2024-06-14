package renetik.android.ui.extensions

import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import androidx.core.view.contains
import renetik.android.ui.extensions.view.add
import renetik.android.ui.extensions.view.remove
import renetik.android.ui.protocol.CSViewInterface

operator fun <View : CSViewInterface> ViewGroup.plusAssign(view: View) {
    add(view.view)
}

fun <View : CSViewInterface> ViewGroup.add(view: View): View {
    add(view.view)
    return view
}

fun <View : CSViewInterface> ViewGroup.add(view: View, index: Int = -1): View {
    add(view.view, index)
    return view
}

fun <View : CSViewInterface> ViewGroup.add(
    view: View, params: LayoutParams, index: Int = -1
): View {
    add(view.view, params, index)
    return view
}

fun ViewGroup.contains(view: CSViewInterface): Boolean = contains(view.view)

fun <View : CSViewInterface> ViewGroup.remove(view: View): View =
    view.also { remove(it.view) }

