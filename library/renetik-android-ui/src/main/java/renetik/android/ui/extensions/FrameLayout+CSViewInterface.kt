package renetik.android.ui.extensions

import android.view.ViewGroup.LayoutParams
import android.widget.FrameLayout
import renetik.android.ui.extensions.view.add
import renetik.android.ui.extensions.view.clear
import renetik.android.ui.protocol.CSViewInterface

fun <View : CSViewInterface> FrameLayout.set(view: View): View {
    clear()
    add(view.view)
    return view
}

fun <View : CSViewInterface> FrameLayout.set(view: View, params: LayoutParams): View {
    clear()
    add(view.view, params)
    return view
}