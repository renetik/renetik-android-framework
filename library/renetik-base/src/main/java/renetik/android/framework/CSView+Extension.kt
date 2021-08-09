package renetik.android.framework

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioGroup
import androidx.annotation.IdRes
import renetik.android.framework.event.CSEventOwner
import renetik.android.view.extensions.*

fun <T : View> CSView<*>.findView(@IdRes id: Int): T? = view.findView(id)

fun CSView<*>.view(@IdRes id: Int, onClick: ((view: View) -> Unit)? = null) =
    view.view(id).apply { onClick?.let { this.onClick(it) } }

fun CSView<*>.views(@IdRes vararg ids: Int): List<View> =
    mutableListOf<View>().apply { for (id in ids) add(view(id)) }

fun CSView<*>.simpleView(@IdRes id: Int) = view.view(id)
fun CSView<*>.editText(@IdRes id: Int) = view.editText(id)
fun CSView<*>.textView(@IdRes id: Int, onClick: ((view: View) -> Unit)? = null) =
    view.textView(id).apply { onClick?.let { this.onClick(it) } }

fun CSView<*>.scrollView(@IdRes id: Int) = view.scrollView(id)
fun CSView<*>.horizontalScroll(@IdRes id: Int) =
    view.horizontalScroll(id)

fun CSView<*>.listView(@IdRes id: Int) = view.listView(id)
fun CSView<*>.radio(@IdRes id: Int) = view.radio(id)

fun CSView<*>.datePicker(@IdRes id: Int) = view.datePicker(id)
fun CSView<*>.numberPicker(@IdRes id: Int) = view.numberPicker(id)
fun CSView<*>.viewGroup(@IdRes id: Int) = view.viewGroup(id)
fun CSView<*>.frame(@IdRes id: Int) = view.frame(id)
fun CSView<*>.linearLayout(@IdRes id: Int) = view.linearLayout(id)
fun CSView<*>.group(@IdRes id: Int) = view.group(id)
fun CSView<*>.spinner(@IdRes id: Int) = view.spinner(id)
fun CSView<*>.search(@IdRes id: Int) = view.search(id)
fun CSView<*>.button(@IdRes id: Int,
                     onClick: ((view: Button) -> Unit)? = null) =
    view.button(id).apply { onClick?.let { this.onClick(it) } }

fun CSView<*>.compound(@IdRes id: Int) = view.compound(id)

//fun CSView<*>.switch(@IdRes id: Int) = view.switch(id)
fun CSView<*>.checkBox(@IdRes id: Int) = view.checkBox(id)
fun CSView<*>.timePicker(@IdRes id: Int) = view.timePicker(id)
fun CSView<*>.webView(@IdRes id: Int) = view.webView(id)
fun CSView<*>.imageView(@IdRes id: Int,
                        onClick: ((view: ImageView) -> Unit)? = null) =
    view.imageView(id).apply { onClick?.let { this.onClick(it) } }

fun CSView<*>.swipeRefresh(@IdRes id: Int) = view.swipeRefresh(id)
fun CSView<*>.seekBar(@IdRes id: Int) = view.seekBar(id)
fun CSView<*>.radioGroup(@IdRes id: Int, onChange: ((buttonId: Int) -> Unit)? = null): RadioGroup =
    view.radioGroup(id).apply { onChange?.let { this.onChange(it) } }

fun CSView<*>.inflateView(layoutId: Int) = inflate<View>(layoutId)

fun <Type : CSView<*>> Type.removeFromSuperview() = apply {
    view.removeFromSuperview()
}

fun <Type> Type.afterLayout(action: (Type) -> Unit)
        where  Type : CSView<*>, Type : CSEventOwner =
    apply { register(view.afterLayout { action(this) }) }


