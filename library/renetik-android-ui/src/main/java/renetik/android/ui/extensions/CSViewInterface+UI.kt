package renetik.android.ui.extensions

import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.IdRes
import androidx.drawerlayout.widget.DrawerLayout
import renetik.android.core.kotlin.notNull
import renetik.android.event.common.CSHasDestruct
import renetik.android.event.common.destruct
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.CSRegistration.Companion.CSRegistration
import renetik.android.event.registration.listenOnce
import renetik.android.event.registration.register
import renetik.android.ui.CSDisplayCutout
import renetik.android.ui.extensions.view.*
import renetik.android.ui.extensions.widget.onChange
import renetik.android.ui.extensions.widget.radioGroup
import renetik.android.ui.protocol.CSViewInterface

fun <T : View> CSViewInterface.findView(@IdRes id: Int): T? = view.findView(id)

@JvmName("viewOfType")
inline fun <reified Type : View> CSViewInterface.view(
    @IdRes id: Int, noinline onClick: ((view: View) -> Unit)? = null
): Type =
    view.view(id).apply { onClick?.let { this.onClick(it) } } as Type

fun CSViewInterface.view(
    @IdRes id: Int, onClick: ((view: View) -> Unit)? = null
): View =
    view.view(id).apply { onClick?.let { this.onClick(it) } }

fun CSViewInterface.viewOrNull(@IdRes id: Int): View? = view.findView(id)

fun CSViewInterface.views(@IdRes vararg ids: Int): List<View> =
    mutableListOf<View>().apply { for (id in ids) add(view(id)) }

fun CSViewInterface.simpleView(@IdRes id: Int) = view.view(id)
fun CSViewInterface.editText(@IdRes id: Int) = view.editText(id)
fun CSViewInterface.textView(@IdRes id: Int, onClick: ((view: View) -> Unit)? = null) =
    view.textView(id).apply { onClick?.let { this.onClick(it) } }

fun CSViewInterface.textViewOrNull(@IdRes id: Int, onClick: ((view: View) -> Unit)? = null) =
    view.findView<TextView>(id)?.apply { onClick?.let { this.onClick(it) } }

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
fun CSViewInterface.button(
    @IdRes id: Int,
    onClick: ((view: Button) -> Unit)? = null
) =
    view.button(id).apply { onClick?.let { this.onClick(it) } }

fun CSViewInterface.compound(@IdRes id: Int): CompoundButton = view.compound(id)

fun CSViewInterface.checkBox(@IdRes id: Int) = view.checkBox(id)
fun CSViewInterface.timePicker(@IdRes id: Int) = view.timePicker(id)
fun CSViewInterface.webView(@IdRes id: Int) = view.webView(id)
fun CSViewInterface.imageView(
    @IdRes id: Int,
    onClick: ((view: ImageView) -> Unit)? = null
) =
    view.imageView(id).apply { onClick?.let { this.onClick(it) } }

fun CSViewInterface.seekBar(@IdRes id: Int) = view.seekBar(id)
fun CSViewInterface.progress(@IdRes id: Int) = view.progress(id)
fun CSViewInterface.radioGroup(
    @IdRes id: Int,
    onChange: ((buttonId: Int) -> Unit)? = null
): RadioGroup =
    view.radioGroup(id).apply { onChange?.let { this.onChange(it) } }

fun <Type : CSViewInterface> Type.removeFromSuperview() = apply { view.removeFromSuperview() }

fun CSViewInterface.afterGlobalLayout(function: () -> Unit): CSRegistration =
    view.afterLayout(this) { function() }

fun <Type> Type.onGlobalFocus(function: (View?, View?) -> Unit): CSRegistration
    where  Type : CSViewInterface =
    register(view.onGlobalFocus { old, new -> function(old, new) })

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
    get() = (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
        view.rootWindowInsets.displayCutout?.let { CSDisplayCutout(it) }
    else null)


val CSViewInterface.hasParentView: Boolean get() = view.parent.notNull

fun CSViewInterface.destroyAndRemoveFromParentWhenDestroyed(parent: CSHasDestruct) {
    listenOnce(parent.eventDestruct) {
        val parentGroup = (view.parent as? ViewGroup)
        if (parentGroup !is AdapterView<*>) parentGroup?.removeView(view)
        destruct()
    }
}

val CSViewInterface.leftMarginInWindow: Int
    get() = view.rectangleInWindow.left

fun CSViewInterface.onHasSize(function: () -> Unit): CSRegistration? =
    view.onHasSize(this) { function() }