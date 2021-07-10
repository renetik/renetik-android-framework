package renetik.android.java.event

@JvmName("eventWithType")
fun <T> event(): CSEvent<T> = CSEventImpl()

fun event(): CSEvent<Unit> = CSEventImpl()

fun CSEvent<Unit>.fire() = fire(Unit)

inline fun CSEvent<Unit>.listen(crossinline function: () -> Unit) =
    this.add { _, _ -> function() }

inline fun <T> CSEvent<T>.listen(crossinline function: (argument: T) -> Unit) =
    this.add { _, argument -> function(argument) }

inline fun <T> CSEvent<T>.listenOnce(crossinline listener: (argument: T) -> Unit) =
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





