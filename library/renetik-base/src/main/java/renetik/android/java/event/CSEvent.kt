package renetik.android.java.event

fun <T> event(): CSEvent<T> = CSEventImpl()

fun CSEvent<Unit>.fire() = fire(Unit)

fun CSEvent<Unit>.listen(function: () -> Unit) =
    this.add { _, _ -> function() }

fun <T> CSEvent<T>.listen(function: (argument: T) -> Unit) =
    this.add { _, argument -> function(argument) }

fun <T> CSEvent<T>.listenOnce(listener: (argument: T) -> Unit) =
    add { registration, argument ->
        registration.cancel()
        listener(argument)
    }

interface CSEvent<T> {

    fun add(listener: (registration: CSEventRegistration, argument: T) -> Unit): CSEventRegistration

    fun fire(argument: T)

    fun clear()

    interface CSEventRegistration {

        var isActive: Boolean

        fun cancel()

        fun event(): CSEvent<*>
    }
}





