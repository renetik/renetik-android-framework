package renetik.android.extensions.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.paris.extensions.style
import renetik.android.view.base.CSView
import renetik.java.extensions.isSet

fun ViewGroup.add(view: CSView<*>) = add(view.view)

fun <ViewType : View> ViewGroup.add(view: ViewType): ViewType {
    view.removeFromSuperview()
    addView(view)
    return view
}

fun <ViewType : View> ViewGroup.add(view: ViewType, layout: ViewGroup.LayoutParams,
                                    init: ((ViewType).() -> Unit)? = null): ViewType =
        add(view, layout, 0, init)

fun <ViewType : View> ViewGroup.add(view: ViewType, layout: ViewGroup.LayoutParams,
                                    styleResource: Int,
                                    init: ((ViewType).() -> Unit)? = null): ViewType {
    view.removeFromSuperview()
    if (styleResource.isSet) view.style(styleResource)
    addView(view, layout)
    init?.invoke(view)
    return view
}

fun <ViewType : View> ViewGroup.add(layoutId: Int): ViewType =
        add(LayoutInflater.from(context).inflate(layoutId, this, false)) as ViewType

fun <T : ViewGroup> T.remove(view: CSView<*>) = remove(view.view)

fun <T : ViewGroup> T.remove(view: View) = apply {
    removeView(view)
}