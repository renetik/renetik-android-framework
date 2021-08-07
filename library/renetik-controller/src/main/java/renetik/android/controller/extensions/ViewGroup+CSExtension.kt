package renetik.android.controller.extensions

import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import renetik.android.framework.CSView
import renetik.android.view.extensions.add
import renetik.android.view.extensions.remove

fun <Controller : CSView<*>> ViewGroup.add(controller: Controller): Controller {
    add(controller.view)
    return controller
}

fun <Controller : CSView<*>> ViewGroup.add(controller: Controller, index: Int = -1): Controller {
    add(controller.view, index)
    return controller
}

fun <Controller : CSView<*>> ViewGroup.add(
    controller: Controller, layout: LayoutParams, index: Int = -1,
): Controller {
    add(controller.view, layout, index)
    return controller
}

fun <T : ViewGroup> T.remove(view: CSView<*>): T {
    remove(view.view)
    return this
}

