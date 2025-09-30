package renetik.android.ui.extensions

import android.view.View
import android.view.ViewGroup.LayoutParams
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import renetik.android.ui.extensions.view.add
import renetik.android.ui.extensions.view.clear
import renetik.android.ui.protocol.CSViewInterface

fun <View : android.view.View> FrameLayout.set(@LayoutRes layout: Int): View {
    clear()
    return add(layoutId = layout)
}

fun <View : CSViewInterface> FrameLayout.set(view: View): View {
    set(view.view)
    return view
}

suspend fun <View : CSViewInterface> FrameLayout.suspendSet(view: View): View {
    set(view.view())
    return view
}

fun FrameLayout.set(view: View, params: LayoutParams? = null): View {
    clear()
    params?.let { add(view, it) } ?: add(view)
    return view
}

fun <View : CSViewInterface> FrameLayout.set(view: View, params: LayoutParams): View {
    clear()
    add(view.view, params)
    return view
}

@JvmName("setNull")
fun <View : CSViewInterface> FrameLayout.set(
    view: View? = null, params: LayoutParams? = null
): View? {
    clear()
    view?.let { params?.let { add(view.view, params) } ?: add(it.view) }
    return view
}