package renetik.android.controller.extensions

import android.content.Intent
import android.view.View
import android.view.ViewParent
import renetik.android.controller.base.CSActivityView
import renetik.android.event.listen
import renetik.android.event.listenOnce
import renetik.android.framework.util.CSReachability

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

fun CSActivityView<*>.onInternetConnected(function: () -> Unit): CSReachability {
    val reachability = CSReachability().start()
    reachability.eventOnConnected.listenOnce { function() }
    eventDestroy.listen { reachability.stop() }
    return reachability
}

fun ViewParent.asActivityView() = ((this as? View)?.tag as? CSActivityView<*>)
fun View.asActivityView() = ((this as? View)?.tag as? CSActivityView<*>)