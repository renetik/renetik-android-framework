package renetik.android.event.dispatch

import renetik.android.core.android.os.CSHandler.mainHandler
import renetik.android.core.kotlin.primitives.min
import renetik.android.event.registration.CSHasRegistrations
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.plus
import kotlin.time.Duration

inline fun CSHasRegistrations.later(
    after: Int, crossinline function: () -> Unit,
): CSRegistration = this + mainHandler.later(after.min(10)) { function() }

inline fun CSHasRegistrations.later(
    after: Duration, crossinline function: () -> Unit,
): CSRegistration = later(after.inWholeMilliseconds.toInt(), function)