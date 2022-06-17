package renetik.android.event.owner

import renetik.android.core.java.lang.isMain
import renetik.android.core.lang.CSMainHandler.postOnMain
import renetik.android.core.lang.CSMainHandler.removePosted
//import renetik.android.event.registration.CSRegistration.Companion.CSRegistration
import renetik.android.core.lang.void
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.CSRegistrations
import renetik.android.event.register
import renetik.android.event.registration.CSRegistrationFunctions
import renetik.android.event.registration.CSRegistrationFunctions.CSRegistration
import renetik.android.event.remove

interface CSEventOwner {
    val registrations: CSRegistrations

    /** later should be here instead of extension so Any.later is not called by mistake
     *  There is strange issue where postOnMain invokes immediately for unknown reason,
     *  so this was rewritten to not fail, even 5ms caused issue on some device so..
     */
//    fun later(delayMilliseconds: Int, function: () -> Unit): CSRegistration {
//        val registration = CSRegistration()
//        postOnMain(delayMilliseconds) {
//            if (registration.isActive) {
//                registration.isActive = false
//                function()
//                remove(registration)
//            }
//        }
//        return register(registration)
//    }

    fun later(delayMilliseconds: Int, function: () -> Unit): CSRegistration {
        lateinit var registration: CSRegistration
        val posted: () -> void = {
            function()
            remove(registration)
        }
        registration = register(CSRegistration(onCancel = { removePosted(posted) }))
        postOnMain(delayMilliseconds, posted)
        return registration
    }

    fun later(function: () -> Unit) = later(5, function)

    /** onMain uses later(5) due to one strange rare multithreading issue
     *  where later function where executed earlier then later returned registration
     */
    fun <T : Any> T.onMain(function: (T).() -> Unit): CSRegistration? =
        if (Thread.currentThread().isMain) {
            function(); null
        } else later(5) { function(this) }
}