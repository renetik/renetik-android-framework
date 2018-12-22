package renetik.android.view.extensions

import android.content.res.ColorStateList
import android.content.res.Resources
import android.view.View
import renetik.android.extensions.*
import renetik.android.java.common.tryAndCatch
import renetik.android.java.common.tryAndWarn

fun <T : View> T.backgroundTint(value: Int) = apply {
    backgroundTintList = ColorStateList.valueOf(context.color(value))
}

fun <T : View> T.background(value: Int) = apply {
    tryAndCatch(Resources.NotFoundException::class, { setBackgroundColor(context.resourceColor(value)) }, {
        tryAndCatch(Resources.NotFoundException::class, { setBackgroundResource(context.resourceFromAttribute(value)) }, {
            tryAndCatch(Resources.NotFoundException::class, { setBackgroundColor(context.colorFromAttribute(value)) },
                    { tryAndWarn { setBackgroundColor(value) } })
        })
    })
}

fun <T : View> T.padding(left: Int, top: Int, right: Int, bottom: Int) = apply {
    setPadding(context.toPixel(left), context.toPixel(top),
            context.toPixel(right), context.toPixel(bottom))
}

fun <T : View> T.padding(value: Int) = apply {
    val pixelValue = context.toPixel(value)
    setPadding(pixelValue, pixelValue, pixelValue, pixelValue)
}

fun <T : View> T.verticalPadding(value: Int) = apply {
    val pixelValue = context.toPixel(value)
    setPadding(paddingLeft, pixelValue, paddingRight, pixelValue)
}

fun <T : View> T.horizontalPadding(value: Int) = apply {
    val pixelValue = context.toPixel(value)
    setPadding(pixelValue, paddingTop, pixelValue, paddingBottom)
}