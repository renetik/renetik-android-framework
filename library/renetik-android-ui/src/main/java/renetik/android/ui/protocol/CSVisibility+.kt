package renetik.android.ui.protocol

import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.onChange
import renetik.android.event.registration.onChangeOnce
import renetik.android.event.registration.waitForFalse
import renetik.android.event.registration.waitForTrue

fun CSVisibility.onShowing(function: (CSRegistration) -> Unit): CSRegistration =
    isVisible.onChange { registration, visible -> if (visible) function(registration) }


fun CSVisibility.onShowingFirstTime(function: () -> Unit) {
    isVisible.onChangeOnce(parent = null, function)
}

fun CSVisibility.onHiding(function: (CSRegistration) -> Unit): CSRegistration =
    isVisible.onChange { registration, visible -> if (!visible) function(registration) }

suspend fun CSVisibility.waitForShow(): Unit = isVisible.waitForTrue()
suspend fun CSVisibility.waitForGone(): Unit = isVisible.waitForFalse()