package renetik.android.java.event

import renetik.android.base.CSApplicationObject.application
import renetik.android.base.CSValueStoreInterface
import renetik.android.java.extensions.primitives.isFalse
import renetik.android.java.extensions.primitives.isTrue

class CSEventProperty<T>(value: T, private val onApply: ((value: T) -> Unit)? = null) {

    private val eventChange: CSEvent<T> = event()
    var previous: T? = null

    var value: T = value
        set(value) {
            if (field == value) return
            previous = field
            field = value
            onApply?.invoke(value)
            eventChange.fire(value)
        }

    fun value(value: T) = apply { this.value = value }

    fun onChange(value: (T) -> Unit) = eventChange.listener(value)

    fun apply() = apply {
        onApply?.invoke(value)
        eventChange.fire(value)
    }
}

fun CSEventProperty<Boolean>.toggle() = apply { value = !value }
fun CSEventProperty<Boolean>.setFalse() = apply { value = false }
fun CSEventProperty<Boolean>.setTrue() = apply { value = true }
fun CSEventProperty<Boolean>.onFalse(function: () -> Unit) =
    onChange { if (it.isFalse) function() }

fun CSEventProperty<Boolean>.onTrue(function: () -> Unit) =
    onChange { if (it.isTrue) function() }

var CSEventProperty<Boolean>.isTrue
    get() = value
    set(newValue) {
        value = newValue
    }
var CSEventProperty<Boolean>.isFalse
    get() = !value
    set(newValue) {
        value = !newValue
    }

object CSEventPropertyFunctions {

    fun <T> property(value: T, onApply: ((value: T) -> Unit)? = null)
            : CSEventProperty<T> = CSEventProperty(value, onApply)

    fun property(
        store: CSValueStoreInterface, key: String, default: String,
        onChange: ((value: String) -> Unit)? = null
    ) = property(store.getString(key, default), onChange)
        .apply { onChange { store.save(key, it) } }

    fun property(key: String, default: String, onApply: ((value: String) -> Unit)? = null) =
        property(application.store, key, default, onApply)

    fun property(
        store: CSValueStoreInterface, key: String, default: Float,
        onApply: ((value: Float) -> Unit)? = null
    ) = property(store.getFloat(key, default), onApply)
        .apply { onChange { store.save(key, it) } }

    fun property(key: String, default: Float, onApply: ((value: Float) -> Unit)? = null) =
        property(application.store, key, default, onApply)

    fun property(
        store: CSValueStoreInterface, key: String, default: Int,
        onApply: ((value: Int) -> Unit)? = null
    ) = property(store.getInt(key, default), onApply)
        .apply { onChange { store.save(key, it) } }

    fun property(key: String, default: Int, onApply: ((value: Int) -> Unit)? = null) =
        property(application.store, key, default, onApply)

    fun property(
        store: CSValueStoreInterface, key: String, default: Boolean,
        onApply: ((value: Boolean) -> Unit)? = null
    ) = property(store.getBoolean(key, default), onApply)
        .apply { onChange { store.save(key, it) } }

    fun property(
        key: String, default: Boolean,
        onApply: ((value: Boolean) -> Unit)? = null
    ): CSEventProperty<Boolean> =
        property(application.store, key, default, onApply)

    fun <T> property(
        store: CSValueStoreInterface, key: String, values: List<T>, default: T,
        onApply: ((value: T) -> Unit)? = null
    ): CSEventProperty<T> {
        val savedValueHashCode = store.getInt(key)
        val value = values.find { it.hashCode() == savedValueHashCode }
        return property(value ?: default, onApply)
            .apply { onChange { store.save(key, it.hashCode()) } }
    }

    fun <T> property(
        key: String, values: List<T>, default: T,
        onApply: ((value: T) -> Unit)? = null
    ) = property(application.store, key, values, default, onApply)

    fun <T> property(
        key: String, values: List<T>, defaultIndex: Int = 0,
        onApply: ((value: T) -> Unit)? = null
    ) = property(application.store, key, values,  values[defaultIndex], onApply)

}