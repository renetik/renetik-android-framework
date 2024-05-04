package renetik.android.ui.protocol

import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.onChange
import renetik.android.event.registration.waitIsFalse
import renetik.android.event.registration.waitIsTrue

fun CSVisibility.onShowing(function: (CSRegistration) -> Unit): CSRegistration =
    isVisible.onChange { registration, visible -> if (visible) function(registration) }

fun CSVisibility.onHiding(function: (CSRegistration) -> Unit): CSRegistration =
    isVisible.onChange { registration, visible -> if (!visible) function(registration) }

suspend fun CSVisibility.waitForShowing(): Unit = isVisible.waitIsTrue()
suspend fun CSVisibility.waitForHiding(): Unit = isVisible.waitIsFalse()