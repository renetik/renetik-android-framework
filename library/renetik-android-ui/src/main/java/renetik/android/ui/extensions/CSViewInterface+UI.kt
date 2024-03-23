package renetik.android.ui.extensions

import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.CompoundButton
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.drawerlayout.widget.DrawerLayout
import renetik.android.core.kotlin.notNull
import renetik.android.core.lang.ArgFunc
import renetik.android.event.common.CSHasDestruct
import renetik.android.event.common.destruct
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.CSRegistration.Companion.CSRegistration
import renetik.android.event.registration.cancel
import renetik.android.event.registration.plus
import renetik.android.event.registration.registerListenOnce
import renetik.android.ui.CSDisplayCutout
import renetik.android.ui.extensions.view.afterGlobalLayout
import renetik.android.ui.extensions.view.checkBox
import renetik.android.ui.extensions.view.compound
import renetik.android.ui.extensions.view.datePicker
import renetik.android.ui.extensions.view.editText
import renetik.android.ui.extensions.view.findView
import renetik.android.ui.extensions.view.frame
import renetik.android.ui.extensions.view.group
import renetik.android.ui.extensions.view.horizontalScroll
import renetik.android.ui.extensions.view.imageView
import renetik.android.ui.extensions.view.linear
import renetik.android.ui.extensions.view.listView
import renetik.android.ui.extensions.view.onClick
import renetik.android.ui.extensions.view.onGlobalFocus
import renetik.android.ui.extensions.view.onHasSize
import renetik.android.ui.extensions.view.progress
import renetik.android.ui.extensions.view.radio
import renetik.android.ui.extensions.view.rectangleInWindow
import renetik.android.ui.extensions.view.removeFromSuperview
import renetik.android.ui.extensions.view.scrollView
import renetik.android.ui.extensions.view.search
import renetik.android.ui.extensions.view.seekBar
import renetik.android.ui.extensions.view.spinner
import renetik.android.ui.extensions.view.textView
import renetik.android.ui.extensions.view.timePicker
import renetik.android.ui.extensions.view.view
import renetik.android.ui.extensions.view.viewGroup
import renetik.android.ui.extensions.view.webView
import renetik.android.ui.protocol.CSViewInterface

fun <T : View> CSViewInterface.findView(@IdRes id: Int): T? = view.findView(id)

@JvmName("viewOfType")
inline fun <reified Type : View> CSViewInterface.view(
    @IdRes id: Int, noinline onClick: ((view: View) -> Unit)? = null
): Type = view.findViewById<Type>(id)!!.apply { onClick?.let { onClick { it(this) } } }

fun CSViewInterface.view(
    @IdRes id: Int, onClick: ((view: View) -> Unit)? = null
): View = view.view(id).apply { onClick?.let { onClick { it(this) } } }

fun CSViewInterface.viewOrNull(@IdRes id: Int): View? = view.findView(id)

fun CSViewInterface.views(@IdRes vararg ids: Int): List<View> =
    mutableListOf<View>().apply { for (id in ids) add(view(id)) }

fun CSViewInterface.simpleView(@IdRes id: Int) = view.view(id)
fun CSViewInterface.editText(@IdRes id: Int) = view.editText(id)

fun CSViewInterface.textView(@IdRes id: Int, onClick: ArgFunc<View>? = null) =
    view.textView(id).apply { onClick?.let { onClick { it(this) } } }

fun CSViewInterface.textViewOrNull(@IdRes id: Int, onClick: ArgFunc<View>? = null) =
    view.findView<TextView>(id)?.apply { onClick?.let { onClick { it(this) } } }

fun CSViewInterface.scrollView(@IdRes id: Int) = view.scrollView(id)
fun CSViewInterface.horizontalScroll(@IdRes id: Int) =
    view.horizontalScroll(id)

fun CSViewInterface.listView(@IdRes id: Int) = view.listView(id)
fun CSViewInterface.radio(@IdRes id: Int) = view.radio(id)

fun CSViewInterface.datePicker(@IdRes id: Int) = view.datePicker(id)

fun CSViewInterface.viewGroup(@IdRes id: Int) = view.viewGroup(id)
fun CSViewInterface.frame(@IdRes id: Int) = view.frame(id)
fun CSViewInterface.drawer(@IdRes id: Int) = view.view(id) as DrawerLayout
fun CSViewInterface.linear(@IdRes id: Int) = view.linear(id)
fun CSViewInterface.group(@IdRes id: Int) = view.group(id)
fun CSViewInterface.spinner(@IdRes id: Int) = view.spinner(id)
fun CSViewInterface.search(@IdRes id: Int) = view.search(id)

fun CSViewInterface.compound(@IdRes id: Int): CompoundButton = view.compound(id)

fun CSViewInterface.checkBox(@IdRes id: Int) = view.checkBox(id)
fun CSViewInterface.timePicker(@IdRes id: Int) = view.timePicker(id)
fun CSViewInterface.webView(@IdRes id: Int) = view.webView(id)

fun CSViewInterface.imageView(
    @IdRes id: Int, onClick: ArgFunc<View>? = null
) = view.imageView(id).apply { onClick?.let { onClick { it(this) } } }

fun CSViewInterface.seekBar(@IdRes id: Int) = view.seekBar(id)
fun CSViewInterface.progress(@IdRes id: Int) = view.progress(id)

fun <Type : CSViewInterface> Type.removeFromSuperview() = apply {
    if (!isDestructed) view.removeFromSuperview()
}

fun CSViewInterface.registerAfterGlobalLayout(function: () -> Unit): CSRegistration {
    lateinit var registration: CSRegistration
    registration = this + view.afterGlobalLayout {
        cancel(registration)
        function()
    }
    return registration
}

fun <Type> Type.onGlobalFocus(function: (View?, View?) -> Unit): CSRegistration
        where  Type : CSViewInterface =
    this + view.onGlobalFocus { old, new -> function(old, new) }

@Suppress("DEPRECATION")
fun CSViewInterface.onSystemUiVisibilityChangeListener(
    function: () -> Unit
): CSRegistration {
    view.setOnSystemUiVisibilityChangeListener { function() }
    return CSRegistration(onCancel = {
        view.setOnSystemUiVisibilityChangeListener(null)
    })
}

val CSViewInterface.displayCutout: CSDisplayCutout?
    get() = (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        view.rootWindowInsets?.displayCutout?.let { CSDisplayCutout(it) }
    } else null)

val CSViewInterface.hasParentView: Boolean get() = view.parent.notNull

fun CSViewInterface.destroyAndRemoveFromParentWhenDestroyed(parent: CSHasDestruct) {
    registerListenOnce(parent.eventDestruct) {
        val parentGroup = (view.parent as? ViewGroup)
        if (parentGroup !is AdapterView<*>) parentGroup?.removeView(view)
        destruct()
    }
}

val CSViewInterface.leftMarginInWindow: Int
    get() = view.rectangleInWindow.left

fun CSViewInterface.registerHasSize(function: () -> Unit): CSRegistration? =
    view.onHasSize(this) { function() }