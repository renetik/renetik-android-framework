package renetik.android.controller.extensions

import android.content.Intent
import android.view.View
import renetik.android.controller.base.CSViewController
import renetik.android.controller.common.navigation

val CSViewController<*>.intent: Intent get() = activity().intent

fun <T : View> CSViewController<T>.push(): CSViewController<T> {
    navigation.push(this)
    return this
}

