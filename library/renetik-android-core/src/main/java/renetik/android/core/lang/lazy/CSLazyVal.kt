package renetik.android.core.lang.lazy

import kotlin.concurrent.Volatile
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class CSLazyVal<T>(
    private val onLoad: () -> T
) : ReadOnlyProperty<Any?, T>, CSLazyProperty<T> {

    private var _value: T? = null

    @Volatile
    private var _isInitialized = false

    override val value: T?
        get() = synchronized(this) {
            if (!_isInitialized) {
                _value = onLoad()
                _isInitialized = true
            }
            return _value!!
        }

    override fun reset() = synchronized(this) {
        _isInitialized = false
        _value = null
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T = value!!

    override fun isInitialized(): Boolean = _isInitialized

    companion object {
        fun <T> lazyVal(initializer: () -> T) = CSLazyVal(initializer)
    }

    //Java compatibility
    fun get() = value
}