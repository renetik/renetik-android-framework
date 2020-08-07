package renetik.android.view.extensions

import android.content.res.ColorStateList
import android.content.res.Resources
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import renetik.android.extensions.*
import renetik.android.java.common.tryAndCatch
import renetik.android.java.common.tryAndWarn

fun <T : View> T.backgroundTint(value: Int) = apply {
    backgroundTintList = ColorStateList.valueOf(context.color(value))
}

@RequiresApi(Build.VERSION_CODES.M)
fun <T : View> T.foregroundTint(value: Int) = apply {
    foregroundTintList = ColorStateList.valueOf(context.color(value))
}

//fun <T : View> T.background(value: Int) = apply { //TODO: Is this really necesary ?
//    tryAndCatch(Resources.NotFoundException::class,
//        { setBackgroundColor(context.resourceColor(value)) },
//        {
//            tryAndCatch(Resources.NotFoundException::class,
//                { setBackgroundResource(context.resourceFromAttribute(value)) },
//                {
//                    tryAndCatch(Resources.NotFoundException::class,
//                        { setBackgroundColor(context.colorFromAttribute(value)) },
//                        { tryAndWarn { setBackgroundColor(value) } })
//                })
//        })
//}

fun <T : View> T.paddingDp(left: Int, top: Int, right: Int, bottom: Int) = apply {
    setPadding(context.toPixel(left), context.toPixel(top),
        context.toPixel(right), context.toPixel(bottom))
}

fun <T : View> T.paddingDp(value: Int) = apply {
    paddingDp(value, value, value, value)
}

fun <T : View> T.verticalPaddingDp(value: Int) = apply {
    val pixelValue = context.toPixel(value)
    setPadding(paddingLeft, pixelValue, paddingRight, pixelValue)
}

fun <T : View> T.horizontalPaddingDp(value: Int) = apply {
    val pixelValue = context.toPixel(value)
    setPadding(pixelValue, paddingTop, pixelValue, paddingBottom)
}