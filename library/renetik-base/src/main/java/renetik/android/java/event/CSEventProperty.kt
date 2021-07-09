package renetik.android.java.event

import renetik.android.framework.CSApplication.Companion.application
import renetik.android.framework.lang.CSProperty
import renetik.android.java.event.CSEvent.CSEventRegistration
import renetik.android.java.extensions.asString
import renetik.android.primitives.empty
import renetik.android.primitives.isFalse
import renetik.android.primitives.isTrue

interface CSEventPropertyInterface<T> : CSProperty<T> {
    fun onBeforeChange(value: (T) -> Unit): CSEventRegistration
    fun onChange(value: (T) -> Unit): CSEventRegistration
    fun value(newValue: T, fireEvents: Boolean = true)
}

open class CSEventProperty<T>(value: T, onChange: ((value: T) -> Unit)? = null) :
    CSEventPropertyInterface<T> {

    private val eventBeforeChange = event<T>()
    private val eventChange = event<T>()
    private var _value: T = value

    init {
        onChange?.let { eventChange.listen(onChange) }
    }

    override var value: T
        get() = _value
        set(value) {
            value(value)
        }

    override fun value(newValue: T, fireEvents: Boolean) {
        if (_value == newValue) return
        if (fireEvents) eventBeforeChange.fire(_value)
        _value = newValue
        if (fireEvents) eventChange.fire(newValue)
    }

    override fun onBeforeChange(value: (T) -> Unit) = eventBeforeChange.listen(value)
    override fun onChange(value: (T) -> Unit) = eventChange.listen(value)
    fun apply() = apply { eventChange.fire(value) }
    override fun toString() = value.toString()
}

fun CSEventProperty<Boolean>.toggle() = apply { value = !value }
fun CSEventProperty<Boolean>.setFalse() = apply { value = false }
fun CSEventProperty<Boolean>.setTrue() = apply { value = true }
fun CSEventProperty<Boolean>.onFalse(function: () -> Unit) =
    onChange { if (it.isFalse) function() }

fun CSEventProperty<Boolean>.onTrue(function: () -> Unit) =
    onChange { if (it.isTrue) function() }

inline var CSEventProperty<Boolean>.isTrue
    get() = value
    set(newValue) {
        value = newValue
    }
inline var CSEventProperty<Boolean>.isFalse
    get() = !value
    set(newValue) {
        value = !newValue
    }

inline var CSEventProperty<String?>.string
    get() = value.asString
    set(newValue) {
        value = newValue
    }

inline val CSEventProperty<Float>.isEmpty get() = value == Float.empty
inline val CSEventProperty<Float>.isSet get() = !this.isEmpty
inline fun CSEventProperty<Float>.ifEmpty(function: (CSEventProperty<Float>) -> Unit) = apply {
    if (this.isEmpty) function(this)
}

inline fun CSEventProperty<Float>.ifSet(function: (CSEventProperty<Float>) -> Unit) = apply {
    if (this.isSet) function(this)
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