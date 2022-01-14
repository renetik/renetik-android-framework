package renetik.android.view

import android.view.LayoutInflater.from
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.view.children
import androidx.core.view.iterator
import renetik.android.framework.event.CSHasParent

val ViewGroup.lastIndex: Int get() = childCount - 1
val ViewGroup.lastChild get() = if (lastIndex >= 0) getChildAt(lastIndex) else null

fun <ViewType : View> ViewGroup.add(
    view: ViewType, index: Int = -1): ViewType {
    view.removeFromSuperview()
    addView(view, index)
    (view.tag as? CSHasParent)?.onAddedToParent()
    return view
}

inline fun <R:Comparable<R>> ViewGroup.sortChildren(crossinline selector: (View) -> R?) = apply {
    val sorted = children.toList().sortedBy(selector)
    removeAllViews()
    sorted.forEach(::addView)
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
fun <ViewType : View> ViewGroup.add(@LayoutRes layoutId: Int, index: Int = -1) =
    add(from(context).inflate(layoutId, this, false), index) as ViewType

fun ViewGroup.removeAt(index: Int): View {
    val view = getChildAt(index)
    removeViewAt(index)
    return view
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

val ViewGroup.subViews
    get() = object : List<View> {
        override val size: Int
            get() = childCount

        override fun contains(element: View): Boolean {
            return children.toList().contains(element)
        }

        override fun containsAll(elements: Collection<View>): Boolean {
            return children.toList().containsAll(elements)
        }

        override fun get(index: Int): View {
            return getChildAt(index)
        }

        override fun indexOf(element: View): Int {
            return indexOfChild(element)
        }

        override fun isEmpty(): Boolean {
            return size == 0
        }

        override fun iterator(): Iterator<View> {
            return (children as ViewGroup).iterator()
        }

        override fun lastIndexOf(element: View): Int {
            return indexOfChild(element)
        }

        override fun listIterator(): ListIterator<View> {
            return children.toList().listIterator()
        }

        override fun listIterator(index: Int): ListIterator<View> {
            return children.toList().listIterator(index)
        }

        override fun subList(fromIndex: Int, toIndex: Int): List<View> {
            return children.toList().subList(fromIndex, toIndex)
        }

    }