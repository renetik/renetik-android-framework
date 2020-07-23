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

fun <T : View> CSViewController<T>.pushAsLast(): CSViewController<T> {
    navigation.pushAsLast(this)
    return this
}

fun <T : View> CSViewController<T>.pushAsLast(key: String): CSViewController<T> {
    navigation.pushAsLast(key, this)
    return this
}

var <T : View> CSViewController<T>.requestedOrientation
    get() = activity().requestedOrientation
    set(value) {
        activity().requestedOrientation = value
    }