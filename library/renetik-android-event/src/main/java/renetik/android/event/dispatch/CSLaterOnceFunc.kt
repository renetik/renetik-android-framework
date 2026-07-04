package renetik.android.event.dispatch

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import renetik.android.core.lang.CSFunc
import renetik.android.event.registration.CSHasRegistrations
import renetik.android.event.registration.CSRegistration
import kotlin.time.Duration

class CSLaterOnceFunc(
    private val parent: CSHasRegistrations,
    private val dispatcher: CoroutineDispatcher = Main,
    val function: suspend () -> Unit,
    private val after: Duration = Duration.ZERO,
) : CSFunc {

    companion object {
        fun CSHasRegistrations.laterOnce(
            after: Duration, function: suspend () -> Unit) =
            CSLaterOnceFunc(this, Main, function, after)

        fun CSHasRegistrations.laterOnce(
            dispatcher: CoroutineDispatcher = Main,
            after: Duration, function: suspend () -> Unit) =
            CSLaterOnceFunc(this, dispatcher, function, after)

        fun CSHasRegistrations.laterOnce(
            dispatcher: CoroutineDispatcher = Main, function: suspend () -> Unit) =
            CSLaterOnceFunc(this, dispatcher, function)

        fun CSHasRegistrations.laterOnce(
            function: suspend () -> Unit) =
            CSLaterOnceFunc(this, Main, function)
    }

    var registration: CSRegistration? = null
    override operator fun invoke() {
        registration?.cancel()
        registration = parent.launch(dispatcher) { delay(after); function() }
    }
}