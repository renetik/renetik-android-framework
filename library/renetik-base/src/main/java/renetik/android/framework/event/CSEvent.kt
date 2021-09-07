package renetik.android.framework.event

@JvmName("eventWithType")
fun <T> event(): CSEvent<T> = CSEventImpl()

fun event(): CSEvent<Unit> = CSEventImpl()

//TODO:  Move event func to CSEvent companion object
interface CSEvent<T> {

    companion object {

    }

    val isListened: Boolean

    fun add(listener: (registration: CSEventRegistration, argument: T) -> Unit): CSEventRegistration

    fun add(listener: CSEventListener<T>): CSEventRegistration

    fun cancel(listener: CSEventListener<T>)

    fun fire(argument: T)

    fun clear()

    @Deprecated("Just for debugging")
    val registrations: List<CSEventRegistration>
}