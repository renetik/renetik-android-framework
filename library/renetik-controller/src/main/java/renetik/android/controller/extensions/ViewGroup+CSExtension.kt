package renetik.android.controller.extensions

import android.view.View
import android.view.ViewGroup
import renetik.android.controller.base.CSView
import renetik.android.view.extensions.add
import renetik.android.view.extensions.remove

fun ViewGroup.add(view: CSView<*>) = add(view.view)

fun <ViewType : View, Controller : CSView<ViewType>> ViewGroup.add(
    controller: Controller, layout: ViewGroup.LayoutParams,
    init: ((ViewType).() -> Unit)? = null
): Controller {
    add(controller.view, layout, init)
    return controller
}

fun <T : ViewGroup> T.remove(view: CSView<*>) = remove(view.view)

