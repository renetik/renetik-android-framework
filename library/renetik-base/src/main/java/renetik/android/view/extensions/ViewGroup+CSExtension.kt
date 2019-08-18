package renetik.android.view.extensions

import android.view.LayoutInflater
import android.view.LayoutInflater.*
import android.view.View
import android.view.ViewGroup
import renetik.android.base.CSView

fun ViewGroup.add(view: CSView<*>) = add(view.view)

fun <ViewType : View> ViewGroup.add(view: ViewType): ViewType {
    view.removeFromSuperview()
    addView(view)
    return view
}

fun <ViewType : View> ViewGroup.add(view: ViewType, layout: ViewGroup.LayoutParams,
                                    init: ((ViewType).() -> Unit)? = null): ViewType {
    view.removeFromSuperview()
    addView(view, layout)
    init?.invoke(view)
    return view
}

//  return add(inflate<ViewType>(layoutId))
fun <ViewType : View> ViewGroup.add(layoutId: Int) =
    from(context).inflate(layoutId, this, true) as ViewType

@Suppress("UNCHECKED_CAST")
fun <ViewType : View> ViewGroup.inflate(layoutId: Int): ViewType =
        from(context).inflate(layoutId, this, false) as ViewType

fun <T : ViewGroup> T.remove(view: CSView<*>) = remove(view.view)

fun <T : ViewGroup> T.remove(view: View) = apply {
    removeView(view)
}