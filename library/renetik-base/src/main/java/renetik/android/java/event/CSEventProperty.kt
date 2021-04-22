package renetik.android.java.event

import renetik.android.base.CSApplication.Companion.application
import renetik.android.java.common.CSProperty
import renetik.android.java.event.CSEvent.CSEventRegistration
import renetik.android.java.extensions.asString
import renetik.android.java.extensions.primitives.isFalse
import renetik.android.java.extensions.primitives.isTrue

interface CSEventPropertyInterface<T> : CSProperty<T> {
    fun onChange(value: (T) -> Unit): CSEventRegistration
}

open class CSEventProperty<T>(value: T, private val onApply: ((value: T) -> Unit)? = null) :
    CSEventPropertyInterface<T> {

    private val eventChange: CSEvent<T> = event()

    private var _value: T = value

    override var value: T
        get() = _value
        set(value) {
            value(value)
        }

    fun value(newValue: T, fireEvents: Boolean = true) = apply {
        if (_value == newValue) return this
        _value = newValue
        if (fireEvents) {
            onApply?.invoke(newValue)
            eventChange.fire(newValue)
        }
    }

    override fun onChange(value: (T) -> Unit) = eventChange.listen(value)

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

var CSEventProperty<String?>.string
    get() = value.asString
    set(newValue) {
        value = newValue
    }

object CSEventPropertyFunctions {

    fun <T> property(value: T, onApply: ((value: T) -> Unit)? = null) =
        CSEventProperty(value, onApply)

    fun property(key: String, default: String, onApply: ((value: String) -> Unit)? = null) =
        application.store.property(key, default, onApply)

    fun property(key: String, default: Float, onApply: ((value: Float) -> Unit)? = null) =
        application.store.property(key, default, onApply)

    fun property(key: String, default: Int, onApply: ((value: Int) -> Unit)? = null) =
        application.store.property(key, default, onApply)

    fun property(
        key: String, default: Boolean,
        onApply: ((value: Boolean) -> Unit)? = null
    ) = application.store.property(key, default, onApply)

    fun <T> property(
        key: String, values: List<T>, default: T,
        onApply: ((value: T) -> Unit)? = null
    ) = application.store.property(key, values, default, onApply)

    fun <T> property(
        key: String, values: List<T>, defaultIndex: Int = 0,
        onApply: ((value: T) -> Unit)? = null
    ) = application.store.property(key, values, values[defaultIndex], onApply)
}