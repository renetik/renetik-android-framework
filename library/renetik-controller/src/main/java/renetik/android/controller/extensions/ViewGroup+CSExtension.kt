package renetik.android.controller.extensions

import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import renetik.android.controller.base.CSView
import renetik.android.framework.event.CSViewInterface
import renetik.android.view.extensions.add
import renetik.android.view.extensions.remove

fun <View : CSViewInterface> ViewGroup.add(view: View): View {
    add(view.view)
    return view
}

fun <View : CSViewInterface> ViewGroup.add(view: View, index: Int = -1): View {
    add(view.view, index)
    return view
}

fun <View : CSViewInterface> ViewGroup.add(view: View, layout: LayoutParams, index: Int = -1): View {
    add(view.view, layout, index)
    return view
}

fun <T : ViewGroup> T.remove(view: CSViewInterface): T {
    remove(view.view)
    return this
}

