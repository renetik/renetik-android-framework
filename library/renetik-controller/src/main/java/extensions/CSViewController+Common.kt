package renetik.android.controller.extensions

import android.view.View
import renetik.android.controller.base.CSViewController
import renetik.android.controller.common.navigation


fun <T : View> CSViewController<T>.push(): CSViewController<T> {
    navigation.push(this)
    return this
}