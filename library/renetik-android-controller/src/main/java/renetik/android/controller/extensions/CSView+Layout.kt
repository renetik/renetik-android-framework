package renetik.android.controller.extensions

import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.updateLayoutParams
import renetik.android.core.extensions.content.displayWidth
import renetik.android.core.extensions.content.dpToPixel
import renetik.android.core.kotlin.primitives.isSet
import renetik.android.core.math.CSPoint
import renetik.android.ui.extensions.view.locationOnScreen
import renetik.android.ui.protocol.CSViewInterface

val CSViewInterface.layoutWidth get() = this.view.layoutParams.width
val CSViewInterface.layoutHeight get() = this.view.layoutParams.height
val CSViewInterface.left get() = this.view.left
val CSViewInterface.top get() = this.view.top
val CSViewInterface.bottom get() = this.view.top + this.view.height
val CSViewInterface.center get() = CSPoint(left + width / 2, top + height / 2)
val CSViewInterface.width get() = view.width
val CSViewInterface.height get() = view.height
val CSViewInterface.locationOnScreen get() = view.locationOnScreen

fun <T : CSViewInterface> T.setPercentAspectWidth(
    view: View, percent: Int
) = apply {
    val onePercent = context.displayWidth / 100.toFloat()
    val wantedWidth = onePercent * percent

    val scalingFactor = wantedWidth / view.layoutParams.width
    val scaledHeight = (view.layoutParams.height * scalingFactor).toInt()

    val layoutParams = view.layoutParams
    layoutParams.width = wantedWidth.toInt()
    layoutParams.height = scaledHeight

    view.layoutParams = layoutParams
}

fun <T : CSViewInterface> T.setPercentWidth(
    view: View, percent: Int, minimal: Int = 0, maximal: Int = 0
) = apply {
    val onePercent = (context.displayWidth / 100).toDouble()
    var wantedSize = (onePercent * percent).toInt()
    if (minimal.isSet && wantedSize < minimal)
        wantedSize = minimal
    else if (maximal.isSet && wantedSize > maximal) wantedSize = maximal
    val layoutParams = view.layoutParams
    layoutParams.width = wantedSize
    view.layoutParams = layoutParams
}

fun <T : CSViewInterface> T.size(width: Int, height: Int) = apply {
    width(width)
    height(height)
}

fun <T : CSViewInterface> T.width(value: Int) = apply {
    val params = view.layoutParams
    params.width = value
    view.layoutParams = params
}

fun <T : CSViewInterface> T.width(value: Float) = width(value.toInt())

fun <T : CSViewInterface> T.widthDp(value: Int) = apply {
    val params = view.layoutParams
    params.width = context.dpToPixel(value)
    view.layoutParams = params
}

fun <T : CSViewInterface> T.widthDp(value: Float) = apply {
    val params = view.layoutParams
    params.width = context.dpToPixel(value)
    view.layoutParams = params
}

fun <T : CSViewInterface> T.height(value: Int) = apply {
    val params = view.layoutParams
    params.height = value
    view.layoutParams = params
}

fun CSViewInterface.layoutGravityCenter() =
    view.updateLayoutParams<FrameLayout.LayoutParams> { gravity = Gravity.CENTER }