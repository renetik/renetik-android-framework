package renetik.android.ui.extensions

import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import androidx.core.view.contains
import renetik.android.core.kotlin.primitives.isTrue
import renetik.android.event.registration.CSHasChangeValue
import renetik.android.event.registration.action
import renetik.android.ui.extensions.view.add
import renetik.android.ui.extensions.view.remove
import renetik.android.ui.protocol.CSViewInterface

fun ViewGroup.addIf(
    property: CSHasChangeValue<Boolean>, function: () -> CSViewInterface) {
    var addedView: CSViewInterface? = null
    property.action {
        if (it.isTrue) addedView = add(function())
        else {
            addedView?.removeFromSuperview()
            addedView = null
        }
    }
}

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

operator fun <View : CSViewInterface> ViewGroup.minusAssign(view: View) {
    remove(view)
}

