package renetik.android.controller.extensions

import android.view.View
import renetik.android.core.extensions.content.displayWidth
import renetik.android.core.extensions.content.dpToPixel
import renetik.android.controller.base.CSView
import renetik.android.core.math.CSPoint
import renetik.android.core.kotlin.primitives.isSet
import renetik.android.view.locationOnScreen

val CSView<*>.layoutWidth get() = this.view.layoutParams.width
val CSView<*>.layoutHeight get() = this.view.layoutParams.height
val CSView<*>.left get() = this.view.left
val CSView<*>.top get() = this.view.top
val CSView<*>.center get() = CSPoint(left + width / 2, top + height / 2)
val CSView<*>.width get() = view.width
val CSView<*>.height get() = view.height
val CSView<*>.locationOnScreen get() = view.locationOnScreen

fun CSView<*>.setPercentAspectWidth(view: View, percent: Int) {
    val onePercent = displayWidth / 100.toFloat()
    val wantedWidth = onePercent * percent

    val scalingFactor = wantedWidth / view.layoutParams.width
    val scaledHeight = (view.layoutParams.height * scalingFactor).toInt()

    val layoutParams = view.layoutParams
    layoutParams.width = wantedWidth.toInt()
    layoutParams.height = scaledHeight

    view.layoutParams = layoutParams
}

fun CSView<*>.setPercentWidth(view: View, percent: Int, minimal: Int = 0, maximal: Int = 0) {
    val onePercent = (displayWidth / 100).toDouble()
    var wantedSize = (onePercent * percent).toInt()
    if (minimal.isSet && wantedSize < minimal)
        wantedSize = minimal
    else if (maximal.isSet && wantedSize > maximal) wantedSize = maximal
    val layoutParams = view.layoutParams
    layoutParams.width = wantedSize
    view.layoutParams = layoutParams
}

fun CSView<*>.size(width: Int, height: Int) = apply {
    width(width)
    height(height)
}

fun CSView<*>.width(value: Int) = apply {
    val params = view.layoutParams
    params.width = value
    view.layoutParams = params
}

fun CSView<*>.width(value: Float) = width(value.toInt())

fun CSView<*>.widthDp(value: Int) = apply {
    val params = view.layoutParams
    params.width = dpToPixel(value)
    view.layoutParams = params
}

fun CSView<*>.widthDp(value: Float) = apply {
    val params = view.layoutParams
    params.width = dpToPixel(value)
    view.layoutParams = params
}

fun CSView<*>.height(value: Int) = apply {
    val params = view.layoutParams
    params.height = value
    view.layoutParams = params
}