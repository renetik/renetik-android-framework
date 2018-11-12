package renetik.android.java.event

fun <T> fire(event: CSEvent<T>, argument: T) {
    event.fire(argument)
}

fun fire(eventVoid: CSEvent<Unit>) {
    eventVoid.fire(Unit)
}

fun <T> event(): CSEvent<T> {
    return CSEventImpl()
}

interface CSEvent<T> {

    fun run(listener: (registration: CSEventRegistration, argument: T) -> Unit): CSEventRegistration

    fun fire(argument: T)

    fun clear()

    interface CSEventRegistration {

        var isActive: Boolean

        fun cancel()

        fun event(): CSEvent<*>
    }
}

fun <T> CSEvent<T>.execute(function: (argument: T) -> Unit): CSEvent.CSEventRegistration {
    return this.run { _, argument -> function(argument) }
}