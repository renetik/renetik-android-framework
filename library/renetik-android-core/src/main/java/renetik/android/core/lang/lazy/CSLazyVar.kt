package renetik.android.core.lang.lazy

import renetik.android.core.lang.atomic.CSAtomic.Companion.atomic
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class CSLazyVar<T>(
    private val onLoad: () -> T
) : ReadWriteProperty<Any?, T>, CSLazyProperty<T> {

    private var _isInitialized by atomic(false)
    override fun isInitialized(): Boolean = _isInitialized

    override var value: T? = null

    override fun reset() = synchronized(this) {
        _isInitialized = false
        value = null
    }

    override fun getValue(
        thisRef: Any?, property: KProperty<*>
    ): T = synchronized(this) {
        if (!_isInitialized) {
            value = onLoad()
            _isInitialized = true
        }
        return value!!
    }

    override fun setValue(
        thisRef: Any?, property: KProperty<*>, value: T
    ) = synchronized(this) {
        _isInitialized = true
        this.value = value
    }

    companion object {
        fun <T> lazyVar(initializer: () -> T) = CSLazyVar(initializer)
    }
}