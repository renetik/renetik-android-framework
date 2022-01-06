package renetik.android.controller.base

import android.view.View
import android.widget.*
import androidx.annotation.IdRes
import renetik.android.framework.event.CSEventOwner
import renetik.android.framework.event.CSViewInterface
import renetik.android.framework.event.property.CSEventProperty
import renetik.android.framework.event.register
import renetik.android.framework.lang.property.setTrue
import renetik.android.framework.lang.property.toggle
import renetik.android.view.*
import renetik.android.widget.onChange
import renetik.android.widget.radioGroup

fun <T : View> CSViewInterface.findView(@IdRes id: Int): T? = view.findView(id)

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
fun CSViewInterface.linear(@IdRes id: Int) = view.linear(id)
fun CSViewInterface.group(@IdRes id: Int) = view.group(id)
fun CSViewInterface.spinner(@IdRes id: Int) = view.spinner(id)
fun CSViewInterface.search(@IdRes id: Int) = view.search(id)
fun CSViewInterface.button(@IdRes id: Int,
                           onClick: ((view: Button) -> Unit)? = null) =
    view.button(id).apply { onClick?.let { this.onClick(it) } }

fun CSViewInterface.compound(@IdRes id: Int) = view.compound(id)

//fun CSView<*>.switch(@IdRes id: Int) = view.switch(id)
fun CSViewInterface.checkBox(@IdRes id: Int) = view.checkBox(id)
fun CSViewInterface.timePicker(@IdRes id: Int) = view.timePicker(id)
fun CSViewInterface.webView(@IdRes id: Int) = view.webView(id)
fun CSViewInterface.imageView(@IdRes id: Int,
                              onClick: ((view: ImageView) -> Unit)? = null) =
    view.imageView(id).apply { onClick?.let { this.onClick(it) } }

fun CSViewInterface.swipeRefresh(@IdRes id: Int) = view.swipeRefresh(id)
fun CSViewInterface.seekBar(@IdRes id: Int) = view.seekBar(id)
fun CSViewInterface.progress(@IdRes id: Int) = view.progress(id)
fun CSViewInterface.radioGroup(@IdRes id: Int,
                               onChange: ((buttonId: Int) -> Unit)? = null): RadioGroup =
    view.radioGroup(id).apply { onChange?.let { this.onChange(it) } }

fun CSView<*>.inflateView(layoutId: Int) = inflate<View>(layoutId)

fun <Type : CSView<*>> Type.removeFromSuperview() = apply {
    view.removeFromSuperview()
}

fun <Type> Type.afterLayout(action: (Type) -> Unit)
        where  Type : CSView<*>, Type : CSEventOwner =
    apply { register(view.afterLayout { action(this) }) }

fun <Type> Type.hasSize(onHasSize: (Type) -> Unit)
        where  Type : CSView<*>, Type : CSEventOwner =
    apply { register(view.hasSize { onHasSize(this) }) }

fun View.asCSView() = asCS<CSView<*>>()
fun View.asCSActivityView() = asCS<CSActivityView<*>>()
fun <CSViewType : CSView<*>> View.asCS() = ((this as? View)?.tag as? CSViewType)

