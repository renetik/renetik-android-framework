package renetik.android.view.extensions

import android.view.LayoutInflater.from
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.view.children

val ViewGroup.lastChild: View?
    get() = if (childCount > 0) getChildAt(childCount - 1) else null


fun <ViewType : View> ViewGroup.add(view: ViewType, index: Int = -1): ViewType {
    view.removeFromSuperview()
    addView(view, index)
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

@Suppress("UNCHECKED_CAST")
fun <ViewType : View> ViewGroup.add(@LayoutRes layoutId: Int): ViewType {
    from(context).inflate(layoutId, this, true)
    return lastChild as ViewType
}

@Suppress("UNCHECKED_CAST")
fun <ViewType : View> ViewGroup.inflate(@LayoutRes layoutId: Int): ViewType =
    from(context).inflate(layoutId, this, false) as ViewType

fun <T : ViewGroup> T.remove(view: View) = apply { removeView(view) }

fun <T : ViewGroup> T.removeSubViews() = apply { removeAllViews() }

fun <T : ViewGroup> T.clear() = apply { removeAllViews() }

fun ViewGroup.childAt(condition: (View) -> Boolean) = children.find(condition)