package renetik.android.ui.protocol

import renetik.android.event.change.onChange
import renetik.android.event.change.onChangeOnce
import renetik.android.event.change.waitForFalse
import renetik.android.event.change.waitForTrue
import renetik.android.event.registration.CSRegistration

fun CSVisibility.onShowing(function: (CSRegistration) -> Unit): CSRegistration =
    isVisibility.onChange { registration, visible -> if (visible) function(registration) }


fun CSVisibility.onShowingFirstTime(function: () -> Unit) {
    isVisibility.onChangeOnce(parent = null, function)
}

fun CSVisibility.onHiding(function: (CSRegistration) -> Unit): CSRegistration =
    isVisibility.onChange { registration, visible -> if (!visible) function(registration) }

suspend fun CSVisibility.waitForShow(): Unit = isVisibility.waitForTrue()
suspend fun CSVisibility.waitForGone(): Unit = isVisibility.waitForFalse()