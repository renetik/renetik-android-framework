package renetik.android.java.event

import renetik.android.base.CSValueStore
import renetik.android.base.CSValueStoreInterface
import renetik.android.base.application
import renetik.android.java.event.CSEvent.CSEventRegistration
import renetik.android.java.extensions.collections.index
import renetik.android.java.extensions.self


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
            apply()
        }

    fun onChange(value: (T) -> Unit) = eventChange.listen(value)

    fun apply() = self {
        onChange?.invoke(value)
        eventChange.fire(value)
    }
}

fun CSEventProperty<Boolean>.toggle() = apply { value = !value }
fun CSEventProperty<Boolean>.setFalse() = apply { value = false }
fun CSEventProperty<Boolean>.setTrue() = apply { value = true }
val CSEventProperty<Boolean>.isTrue get() = value
val CSEventProperty<Boolean>.isFalse get() = !value

fun property(store: CSValueStoreInterface, key: String, default: Int,
             onApply: ((value: Int) -> Unit)? = null) =
    CSEventProperty(store.getInt(key, default), onApply)
        .apply { onChange { store.save(key, it) } }

fun property(key: String, default: Int,
             onApply: ((value: Int) -> Unit)? = null): CSEventProperty<Int> =
    property(application.store, key, default, onApply)

fun property(store: CSValueStoreInterface, key: String, default: Boolean,
             onApply: ((value: Boolean) -> Unit)? = null) =
    CSEventProperty(store.getBoolean(key, default), onApply)
        .apply { onChange { store.save(key, it) } }

fun property(key: String, default: Boolean,
             onApply: ((value: Boolean) -> Unit)? = null): CSEventProperty<Boolean> =
    property(application.store, key, default, onApply)

fun <T> property(store: CSValueStoreInterface, key: String, values: List<T>, defaultIndex: Int,
                 onApply: ((value: T) -> Unit)? = null): CSEventProperty<T> =
    CSEventProperty(values[store.getInt(key, defaultIndex)], onApply)
        .apply { onChange { store.save(key, values.indexOf(it)) } }

fun <T> property(store: CSValueStoreInterface, key: String, values: List<T>, default: T,
                 onApply: ((value: T) -> Unit)? = null): CSEventProperty<T> =
    property(store, key, values, values.index(default) ?: 0, onApply)

fun <T> property(key: String, values: List<T>, defaultIndex: Int = 0,
                 onApply: ((value: T) -> Unit)? = null): CSEventProperty<T> =
    property(application.store, key, values, defaultIndex, onApply)

fun <T> property(key: String, values: List<T>, default: T,
                 onApply: ((value: T) -> Unit)? = null): CSEventProperty<T> =
    property(application.store, key, values, default, onApply)

fun <T> CSEventProperty<T>.store(store: CSValueStore, key: String,
                                 values: List<T>, defaultValue: T) {
    val defaultValueHashCode = store.getInt(key, defaultValue.hashCode())
    values.find { it.hashCode() == defaultValueHashCode }?.let { value = it }
    onChange { store.save(key, it.hashCode()) }
}

fun <T> CSEventProperty<T>.store(key: String, values: List<T>, defaultValue: T) =
    store(application.store, key, values, defaultValue)



