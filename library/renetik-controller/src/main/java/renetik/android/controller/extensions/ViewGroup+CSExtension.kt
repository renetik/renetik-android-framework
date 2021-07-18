package renetik.android.controller.extensions

import android.view.ViewGroup
import renetik.android.controller.base.CSView
import renetik.android.view.extensions.add
import renetik.android.view.extensions.remove

fun <Controller : CSView<*>> ViewGroup.add(controller: Controller): Controller {
    add(controller.view)
    controller.onAddedToParent()
    return controller
}

fun <Controller : CSView<*>> ViewGroup.add(controller: Controller, index: Int = -1): Controller {
    add(controller.view, index)
    controller.onAddedToParent()
    return controller
}

fun <Controller : CSView<*>> ViewGroup.add(
    controller: Controller, layout: ViewGroup.LayoutParams, index: Int = -1,
): Controller {
    add(controller.view, layout, index)
    controller.onAddedToParent()
    return controller
}

fun <T : ViewGroup> T.remove(view: CSView<*>): T {
    remove(view.view)
    view.onRemovedFromParent()
    return this
}

