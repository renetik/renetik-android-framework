package renetik.android.core.lang.lazy

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class CSLazyNullableVar<T>(
    val didSet: (T) -> Unit, initializer: () -> T
) : ReadWriteProperty<Any?, T> {
    private object InitialValue

    private var isSet = false
    private val lazyValue by lazy(initializer)
    private var value: Any? = InitialValue

    override fun getValue(
        thisRef: Any?, property: KProperty<*>
    ): T = synchronized(this) {
        if (!isSet) return lazyValue
        @Suppress("UNCHECKED_CAST") return value as T
    }

    override fun setValue(
        thisRef: Any?, property: KProperty<*>, value: T
    ) = synchronized(this) {
        this.value = value
        isSet = true
        didSet(value)
    }

    companion object {
        fun <T> lazyNullableVar(didSet: (T) -> Unit = { }, initializer: () -> T) =
            CSLazyNullableVar(didSet, initializer)
    }
}

