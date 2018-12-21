package renetik.android.extensions.view

import android.content.res.ColorStateList
import android.content.res.Resources
import android.view.View
import android.view.ViewGroup.LayoutParams
import com.airbnb.paris.extensions.style
import renetik.android.extensions.*
import renetik.android.base.application
import renetik.java.lang.tryAndCatch
import renetik.java.lang.tryAndWarn

fun <T : View> T.init(init: ((T).() -> Unit)? = null) = apply {
    init?.invoke(this)
}

fun <T : View> T.init(params: LayoutParams, init: ((T).() -> Unit)? = null) = apply {
    layoutParams = params
    init(init)
}

fun <T : View> T.init(style: Int, init: ((T).() -> Unit)? = null) = apply {
    style(style)
    init(init)
}

fun <T : View> T.init(params: LayoutParams, style: Int, init: ((T).() -> Unit)? = null) = apply {
    layoutParams = params
    style(style)
    init(init)
}

fun <T : View> T.backgroundTint(value: Int) = apply {
    backgroundTintList = ColorStateList.valueOf(application.color(value))
}

fun <T : View> T.background(value: Int) = apply {
    tryAndCatch(Resources.NotFoundException::class, { setBackgroundColor(application.resourceColor(value)) }, {
        tryAndCatch(Resources.NotFoundException::class, { setBackgroundResource(context.resourceFromAttribute(value)) }, {
            tryAndCatch(Resources.NotFoundException::class, { setBackgroundColor(context.colorFromAttribute(value)) },
                    { tryAndWarn { setBackgroundColor(value) } })
        })
    })
}

fun <T : View> T.padding(left: Int, top: Int, right: Int, bottom: Int) = apply {
    setPadding(application.toPixel(left), application.toPixel(top),
            application.toPixel(right), application.toPixel(bottom))
}

fun <T : View> T.padding(value: Int) = apply {
    val pixelValue = application.toPixel(value)
    setPadding(pixelValue, pixelValue, pixelValue, pixelValue)
}

fun <T : View> T.verticalPadding(value: Int) = apply {
    val pixelValue = application.toPixel(value)
    setPadding(paddingLeft, pixelValue, paddingRight, pixelValue)
}

fun <T : View> T.horizontalPadding(value: Int) = apply {
    val pixelValue = application.toPixel(value)
    setPadding(pixelValue, paddingTop, pixelValue, paddingBottom)
}