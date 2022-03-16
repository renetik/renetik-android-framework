package renetik.android.framework.event

import renetik.java.lang.isMain

interface CSEventOwner {
    val eventRegistrations: CSEventRegistrations

    // later should be here instead of extension so Any.later is not called by mistake
    fun later(delayMilliseconds: Int, function: () -> Unit): CSRegistration {
        lateinit var registration: CSRegistration
        registration = register(renetik.kotlin.later(delayMilliseconds) {
            function()
            remove(registration)
        })
        return registration
    }

    fun later(function: () -> Unit) = later(5, function)

    // onMain uses later(5) due to one strange rare multithreading issue
    // where later function where executed earlier then later returned registration
    fun <T : Any> T.onMain(function: (T).() -> Unit): CSRegistration? =
        if (Thread.currentThread().isMain) {
            function(); null
        } else
            later(5) { function(this) }
}