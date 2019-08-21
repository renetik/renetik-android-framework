package renetik.android.view.extensions

import android.view.LayoutInflater.from
import android.view.View
import android.view.ViewGroup
import renetik.android.base.CSView

fun ViewGroup.add(view: CSView<*>) = add(view.view)

val ViewGroup.lastChild: View?
    get() = if (childCount > 0) getChildAt(childCount - 1) else null


fun <ViewType : View> ViewGroup.add(view: ViewType): ViewType {
    view.removeFromSuperview()
    addView(view)
    return view
}

fun <ViewType : View> ViewGroup.add(
    view: ViewType, layout: ViewGroup.LayoutParams,
    init: ((ViewType).() -> Unit)? = null
): ViewType {
    view.removeFromSuperview()
    addView(view, layout)
    init?.invoke(view)
    return view
}

fun <ViewType : View> ViewGroup.add(layoutId: Int): ViewType {
    from(context).inflate(layoutId, this, true)
    return lastChild as ViewType
}

@Suppress("UNCHECKED_CAST")
fun <ViewType : View> ViewGroup.inflate(layoutId: Int): ViewType =
    from(context).inflate(layoutId, this, false) as ViewType

fun <T : ViewGroup> T.remove(view: CSView<*>) = remove(view.view)

fun <T : ViewGroup> T.remove(view: View) = apply {
    removeView(view)
}