package renetik.android.ui.protocol

import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.action
import renetik.android.event.registration.actionFalse
import renetik.android.event.registration.actionTrue
import renetik.android.event.registration.onChange

fun CSVisibility.onShowing(function: (CSRegistration) -> Unit): CSRegistration =
    isVisible.onChange { registration, visible -> if (visible) function(registration) }

fun CSVisibility.onHiding(function: (CSRegistration) -> Unit): CSRegistration =
    isVisible.onChange { registration, visible -> if (!visible) function(registration) }