package renetik.android.ui.protocol

import renetik.android.event.listen
import renetik.android.event.registration.CSRegistration

fun CSVisibility.onShowing(function: (CSRegistration) -> Unit): CSRegistration =
    eventVisibility.listen { registration, visible -> if (visible) function(registration) }

fun CSVisibility.onHiding(function: () -> Unit): CSRegistration = onHiding { _ -> function() }

fun CSVisibility.onHiding(function: (CSRegistration) -> Unit): CSRegistration =
    eventVisibility.listen { registration, visible -> if (!visible) function(registration) }

fun CSVisibility.onVisibility(function: (Boolean) -> Unit): CSRegistration =
    eventVisibility.listen(function)

fun CSVisibility.onVisibility(function: (CSRegistration, Boolean) -> Unit): CSRegistration =
    eventVisibility.listen(function)

fun CSVisibility.whileShowingTrue(function: (Boolean) -> Unit): CSRegistration {
    if (isVisible) function(true)
    return eventVisibility.listen { if (it) function(true) else function(false) }
}