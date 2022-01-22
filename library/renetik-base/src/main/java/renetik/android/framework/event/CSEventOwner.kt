package renetik.android.framework.event

interface CSEventOwner {
    val eventRegistrations: CSEventRegistrations

    // later should be here instead of extension so Any.later is not called by mistake
    fun later(delayMilliseconds: Int = 0, function: () -> Unit): CSEventRegistration {
        lateinit var registration: CSEventRegistration
        registration = register(renetik.kotlin.later(delayMilliseconds) {
            function()
            remove(registration)
        })
        return registration
    }

    fun later(function: () -> Unit) = later(0, function)
}