package renetik.android.ui.protocol

import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.onChange
import renetik.android.event.registration.onChangeOnce
import renetik.android.event.registration.waitIsFalse
import renetik.android.event.registration.waitIsTrue

fun CSVisibility.onShowing(function: (CSRegistration) -> Unit): CSRegistration =
    isVisible.onChange { registration, visible -> if (visible) function(registration) }


fun CSVisibility.onShowingFirstTime(function: () -> Unit): CSRegistration =
    isVisible.onChangeOnce(function)

fun CSVisibility.onHiding(function: (CSRegistration) -> Unit): CSRegistration =
    isVisible.onChange { registration, visible -> if (!visible) function(registration) }

suspend fun CSVisibility.waitForShow(): Unit = isVisible.waitIsTrue()
suspend fun CSVisibility.waitForGone(): Unit = isVisible.waitIsFalse()