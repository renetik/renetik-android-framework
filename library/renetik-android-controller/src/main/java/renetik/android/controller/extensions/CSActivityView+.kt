package renetik.android.controller.extensions

import android.content.Intent
import android.view.View
import android.view.ViewParent
import renetik.android.controller.base.CSActivityView

val CSActivityView<*>.intent: Intent get() = activity().intent

fun <T : View, ViewController : CSActivityView<T>>
        ViewController.push() = apply { navigation!!.push(this) }

fun <T : View, ViewController : CSActivityView<T>>
        ViewController.pushMain() = push("mainController")

fun <T : View, ViewController : CSActivityView<T>>
        ViewController.pushAsLast() = apply { navigation!!.pushAsLast(this) }

fun <T : View, ViewController : CSActivityView<T>>
        ViewController.push(pushKey: String) =
    apply { navigation!!.push(pushKey, this) }

var <T : View> CSActivityView<T>.requestedOrientation
    get() = activity().requestedOrientation
    set(value) {
        activity().requestedOrientation = value
    }

fun ViewParent.asActivityView() = ((this as? View)?.tag as? CSActivityView<*>)
fun View.asActivityView() = ((this as? View)?.tag as? CSActivityView<*>)