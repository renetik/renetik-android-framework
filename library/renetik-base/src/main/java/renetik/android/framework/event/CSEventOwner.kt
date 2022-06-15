package renetik.android.framework.event

import renetik.android.framework.util.CSMainHandler.postOnMain
import renetik.java.lang.isMain

interface CSEventOwner {
    val registrations: CSRegistrations

    /** later should be here instead of extension so Any.later is not called by mistake
     *  There is strange issue where postOnMain invokes immediately for unknown reason,
     *  so this was rewritten to not fail, even 5ms caused issue on some device so..
     */
    fun later(delayMilliseconds: Int, function: () -> Unit): CSRegistration {
        val registration = CSRegistration.construct()
        postOnMain(delayMilliseconds) {
            if (registration.isActive) {
                registration.isActive = false
                function()
                remove(registration)
            }
        }
/*        lateinit var registration: CSRegistration
        registration = register(renetik.kotlin.later(delayMilliseconds) {
            function()
            remove(registration)
        })*/
        return register(registration)
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