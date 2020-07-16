package renetik.android.java.event

import renetik.android.base.CSValueStore
import renetik.android.base.CSValueStoreInterface
import renetik.android.java.event.CSEvent.CSEventRegistration


fun <T> event(): CSEvent<T> {
    return CSEventImpl()
}

fun CSEvent<Unit>.fire() = fire(Unit)

fun <T> CSEvent<T>.register(listener: (argument: T) -> Unit): CSEventRegistration {
    return this.add { _, argument -> listener(argument) }
}

fun CSEvent<Unit>.listen(listener: () -> Unit): CSEventRegistration {
    return this.add { _, _ -> listener() }
}

fun <T> CSEvent<T>.listen(listener: (T) -> Unit): CSEventRegistration {
    return this.add { _, argument -> listener(argument) }
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

fun <T> CSEvent<T>.runOnce(listener: (registration: CSEventRegistration, argument: T) -> Unit): CSEventRegistration {
    return add { registration, argument ->
        registration.cancel()
        listener(registration, argument)
    }
}

class CSEventProperty<T>(value: T, private val onChange: ((value: T) -> Unit)? = null) {

    private val eventChange: CSEvent<T> = event()

    var value: T = value
        set(value) {
            if (field == value) return
            field = value
            onChange?.invoke(value)
            eventChange.fire(value)
        }

    fun onChange(value: (T) -> Unit) = eventChange.listen(value)
}

fun property(store: CSValueStoreInterface, key: String, default: Int,
             onChange: ((value: Int) -> Unit)? = null): CSEventProperty<Int> =
    CSEventProperty(store.getInt(key, default), onChange).apply { onChange { store.save(key, it) } }

//TODO: Store by hashcode not index
fun <T> property(store: CSValueStoreInterface, key: String, values: List<T>, defaultIndex: Int,
                 onChange: ((value: T) -> Unit)? = null): CSEventProperty<T> =
    CSEventProperty(values[store.getInt(key, defaultIndex)], onChange)
        .apply { onChange { store.save(key, values.indexOf(it)) } }

fun <T> CSEventProperty<T>.store(store: CSValueStore, key: String,
                                 values: List<T>, defaultValue: T) {
    val defaultValueHashCode = store.getInt(key, defaultValue.hashCode())
    values.find { it.hashCode() == defaultValueHashCode }?.let { value = it }
    onChange { store.save(key, it.hashCode()) }
}



