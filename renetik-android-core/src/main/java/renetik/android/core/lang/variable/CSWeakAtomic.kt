package renetik.android.core.lang.variable

import java.lang.ref.WeakReference
import java.util.concurrent.atomic.AtomicReference
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class CSWeakAtomic<T>(value: T? = null) : CSVariable<T?>, ReadWriteProperty<Any?, T?> {

    private val ref = AtomicReference<WeakReference<T>?>(value?.let { WeakReference(it) })

    override var value: T?
        get() = ref.get()?.get()
        set(value) = ref.set(value?.let(::WeakReference))

    override fun getValue(thisRef: Any?, property: KProperty<*>): T? = value
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        this.value = value
    }

    override fun getAndSet(newValue: T?): T? {
        val newRef = newValue?.let { WeakReference(it) }
        val prevRef = ref.getAndSet(newRef)
        return prevRef?.get()
    }

    fun compareAndSet(expected: T?, update: T?): Boolean {
        while (true) {
            val currentRef = ref.get()
            val current = currentRef?.get()
            if (current != expected) return false
            val newRef = update?.let { WeakReference(it) }
            if (ref.compareAndSet(currentRef, newRef)) return true
        }
    }

    fun clear(): T? = getAndSet(null)

    companion object {
        fun <T> weakAtomic(value: T? = null) = CSWeakAtomic(value)
    }
}