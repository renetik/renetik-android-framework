package renetik.android.controller.extensions

import android.content.Intent
import android.view.View
import renetik.android.controller.base.CSViewController
import renetik.android.controller.common.CSNavigationInstance.navigation
import renetik.android.view.extensions.afterLayout

val CSViewController<*>.intent: Intent get() = activity().intent

fun <T : View, ViewController : CSViewController<T>>
        ViewController.push() = apply { navigation.push(this) }

fun <T : View, ViewController : CSViewController<T>>
        ViewController.pushAsLast() = apply { navigation.pushAsLast(this) }

fun <T : View, ViewController : CSViewController<T>>
        ViewController.pushAsLast(key: String) = apply { navigation.pushAsLast(key, this) }

var <T : View> CSViewController<T>.requestedOrientation
    get() = activity().requestedOrientation
    set(value) {
        activity().requestedOrientation = value
    }