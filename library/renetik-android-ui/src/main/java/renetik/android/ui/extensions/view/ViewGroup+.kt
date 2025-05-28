package renetik.android.ui.extensions.view

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.view.children
import androidx.core.view.isNotEmpty
import renetik.android.ui.extensions.inflate
import renetik.android.ui.protocol.CSHasParentView

operator fun ViewGroup.get(index: Int): View = getChildAt(index)

inline fun <R : Comparable<R>> ViewGroup.sortChildren(
    crossinline selector: (View) -> R?
) = sortChildren { _, view -> selector(view) }

inline fun <R : Comparable<R>> ViewGroup.sortChildren(
    crossinline selector: (Int, View) -> R?
) = apply {
    val sorted = children.toList().withIndex().sortedBy { selector(it.index, it.value) }
    removeAllViews()
    sorted.map(IndexedValue<View>::value).forEach { addView(it) }
}

val ViewGroup.lastIndex: Int get() = childCount - 1
val ViewGroup.firstChild get() = if (isNotEmpty()) getChildAt(0) else null
val ViewGroup.lastChild get() = if (lastIndex >= 0) getChildAt(lastIndex) else null

fun <ViewType : View> ViewGroup.add(
    view: ViewType, index: Int = -1
): ViewType {
    view.removeFromSuperview()
    addView(view, index)
    (view.tag as? CSHasParentView)?.onAddedToParentView()
    return view
}

fun <ViewType : View> ViewGroup.add(
    view: ViewType, layout: ViewGroup.LayoutParams, index: Int = -1,
): ViewType {
    view.removeFromSuperview()
    addView(view, index, layout)
    (view.tag as? CSHasParentView)?.onAddedToParentView()
    return view
}

fun <ViewType : View> ViewGroup.add(@LayoutRes layoutId: Int, index: Int = -1) =
    add(inflate(layoutId) as ViewType, index)

fun <ViewType : View> ViewGroup.add(
    @LayoutRes layoutId: Int,
    layout: ViewGroup.LayoutParams, index: Int = -1
) = add(context.inflate(layoutId, this) as ViewType, layout, index)

fun ViewGroup.removeAt(index: Int): View = getChildAt(index).also { removeViewAt(index) }

fun <ViewType : View> ViewGroup.inflate(@LayoutRes layoutId: Int): ViewType =
    context.inflate(layoutId, this) as ViewType

fun <T : ViewGroup> T.remove(view: View) = apply {
    removeView(view)
    (view.tag as? CSHasParentView)?.onRemovedFromParentView()
}

fun <T : ViewGroup> T.removeSubViews() = apply { clear() }

fun <T : ViewGroup> T.clear() = apply {
    children.forEach { (it.tag as? CSHasParentView)?.onRemovedFromParentView() }
    removeAllViews()
}

fun ViewGroup.childAt(condition: (View) -> Boolean) = children.find(condition)

fun ViewGroup.removeViews(from: Int) = removeViews(from, childCount - from)

val ViewGroup.subViews: List<View> get() = children.toList()