package renetik.android.controller.base

import android.hardware.SensorManager.SENSOR_DELAY_NORMAL
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.view.OrientationEventListener
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.drawerlayout.widget.DrawerLayout
import renetik.android.core.extensions.content.CSDisplayOrientation
import renetik.android.core.extensions.content.orientation
import renetik.android.core.kotlin.notNull
import renetik.android.core.kotlin.primitives.isTrue
import renetik.android.event.registration.*
import renetik.android.event.registration.CSRegistration.Companion.CSRegistration
import renetik.android.ui.extensions.view.*
import renetik.android.ui.extensions.widget.onChange
import renetik.android.ui.extensions.widget.radioGroup
import renetik.android.ui.protocol.CSViewInterface

fun <T : View> CSViewInterface.findView(@IdRes id: Int): T? = view.findView(id)

@JvmName("viewOfType")
inline fun <reified Type : View> CSViewInterface.view(
    @IdRes id: Int, noinline onClick: ((view: View) -> Unit)? = null) =
    view.view(id).apply { onClick?.let { this.onClick(it) } } as Type

fun CSViewInterface.view(@IdRes id: Int, onClick: ((view: View) -> Unit)? = null) =
    view.view(id).apply { onClick?.let { this.onClick(it) } }

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

//fun CSViewInterface.numberPicker(@IdRes id: Int) = view.numberPicker(id)
fun CSViewInterface.viewGroup(@IdRes id: Int) = view.viewGroup(id)
fun CSViewInterface.frame(@IdRes id: Int) = view.frame(id)
fun CSViewInterface.drawer(@IdRes id: Int) = view.view(id) as DrawerLayout
fun CSViewInterface.linear(@IdRes id: Int) = view.linear(id)
fun CSViewInterface.group(@IdRes id: Int) = view.group(id)
fun CSViewInterface.spinner(@IdRes id: Int) = view.spinner(id)
fun CSViewInterface.search(@IdRes id: Int) = view.search(id)
fun CSViewInterface.button(@IdRes id: Int,
                           onClick: ((view: Button) -> Unit)? = null) =
    view.button(id).apply { onClick?.let { this.onClick(it) } }

fun CSViewInterface.compound(@IdRes id: Int) = view.compound(id)

fun CSViewInterface.checkBox(@IdRes id: Int) = view.checkBox(id)
fun CSViewInterface.timePicker(@IdRes id: Int) = view.timePicker(id)
fun CSViewInterface.webView(@IdRes id: Int) = view.webView(id)
fun CSViewInterface.imageView(@IdRes id: Int,
                              onClick: ((view: ImageView) -> Unit)? = null) =
    view.imageView(id).apply { onClick?.let { this.onClick(it) } }

fun CSViewInterface.seekBar(@IdRes id: Int) = view.seekBar(id)
fun CSViewInterface.progress(@IdRes id: Int) = view.progress(id)
fun CSViewInterface.radioGroup(@IdRes id: Int,
                               onChange: ((buttonId: Int) -> Unit)? = null): RadioGroup =
    view.radioGroup(id).apply { onChange?.let { this.onChange(it) } }

fun CSView<*>.inflateView(layoutId: Int) = inflate<View>(layoutId)

fun <Type : CSView<*>> Type.removeFromSuperview() = apply {
    view.removeFromSuperview()
}

fun <Type> Type.afterGlobalLayout(function: () -> Unit)
        where  Type : CSView<*>, Type : CSHasRegistrations =
    view.afterGlobalLayout(this) { function() }

fun <Type> Type.onGlobalFocus(function: (View?, View?) -> Unit)
        where  Type : CSView<*>, Type : CSHasRegistrations {
    register(view.onGlobalFocus { old, new -> function(old, new) })
}

fun <Type> Type.hasSize(function: (Type) -> Unit)
        where  Type : CSView<*>, Type : CSHasRegistrations = apply {
    view.hasSize(this) { function(this) }
}

fun <Type : CSView<*>> Type.disabledIf(condition: Boolean) = apply { isEnabled = !condition }

fun <Type : CSView<*>> Type.disabledByAlpha(condition: Boolean = true, disable: Boolean = true) {
    if (disable) disabledIf(condition)
    view.alphaToDisabled(condition)
}

fun View.asCSView() = asCS<CSView<*>>()

fun View.asCSActivityView() = asCS<CSActivityView<*>>()

@Suppress("UNCHECKED_CAST")
fun <CSViewType : CSView<*>> View.asCS() = this.tag as? CSViewType

val CSView<*>.displayCutout: CSDisplayCutout?
    get() = (if (VERSION.SDK_INT >= VERSION_CODES.P)
        view.rootWindowInsets.displayCutout?.let { CSDisplayCutout(it) }
    else null)

fun CSView<*>.onOrientationChange(
    function: (CSRegistration, CSDisplayOrientation) -> Unit): CSRegistration {
    lateinit var registration: CSRegistration
    registration = onOrientationChange { orientation ->
        function(registration, orientation)
    }
    return registration
}

fun CSView<*>.onOrientationChange(
    function: (CSDisplayOrientation) -> Unit): CSRegistration {
    var currentOrientation = orientation
    var afterGlobalLayoutRegistration: CSRegistration? = null
    val listener = object : OrientationEventListener(this, SENSOR_DELAY_NORMAL) {
        override fun onOrientationChanged(orientation: Int) {
            if (afterGlobalLayoutRegistration?.isActive.isTrue)
                cancel(afterGlobalLayoutRegistration)
            afterGlobalLayoutRegistration = afterGlobalLayout {
                if (this@onOrientationChange.orientation != currentOrientation) {
                    currentOrientation = this@onOrientationChange.orientation
                    function(this@onOrientationChange.orientation)
                }
            }
        }
    }
    return register(CSRegistration(onResume = { listener.enable() },
        onPause = { listener.disable() }).start())
}

val CSView<*>.hasParentView: Boolean get() = view.parent.notNull

fun <T : CSView<*>> T.reusable() = apply { lifecycleStopOnRemoveFromParentView = false }
