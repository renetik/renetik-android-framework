package renetik.android.event.util

import renetik.android.core.lang.CSMainHandler.postOnMain
import renetik.android.core.lang.CSMainHandler.removePosted
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.CSRegistrationFunctions.CSRegistration

object CSLater {
    fun later(
        delayMilliseconds: Int, function: () -> Unit): CSRegistration {
        postOnMain(delayMilliseconds, function)
        return CSRegistration(onCancel = { removePosted(function) })
    }

    /** Later uses default of 5 due to one strange rare multithreading issue
     *  where later function where executed earlier then later returned registration
     */
    fun later(function: () -> Unit) = later(5, function)
}