package renetik.android.view.extensions

import android.view.LayoutInflater.from
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.view.children
import renetik.android.framework.event.CSHasParent

val ViewGroup.lastChild: View?
    get() = if (childCount > 0) getChildAt(childCount - 1) else null

fun <ViewType : View> ViewGroup.add(
    view: ViewType, index: Int = -1): ViewType {
    view.removeFromSuperview()
    addView(view, index)
    (view.tag as? CSHasParent)?.onAddedToParent()
    return view
}

fun <ViewType : View> ViewGroup.add(
    view: ViewType, layout: ViewGroup.LayoutParams, index: Int = -1,
): ViewType {
    view.removeFromSuperview()
    addView(view, index, layout)
    (view.tag as? CSHasParent)?.onAddedToParent()
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

fun <T : ViewGroup> T.remove(view: View) = apply {
    removeView(view)
    (view.tag as? CSHasParent)?.onRemovedFromParent()
}

fun <T : ViewGroup> T.removeSubViews() = apply { clear() }

fun <T : ViewGroup> T.clear() = apply {
    children.forEach { (it.tag as? CSHasParent)?.onRemovedFromParent() }
    removeAllViews()
}

fun ViewGroup.childAt(condition: (View) -> Boolean) = children.find(condition)