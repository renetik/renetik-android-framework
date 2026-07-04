package renetik.android.event.property

import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.dispatch.onMain
import renetik.android.event.lifecycle.CSHasDestruct
import renetik.android.event.registration.CSRegistration
import java.lang.ref.WeakReference
import java.util.concurrent.atomic.AtomicReference
import kotlin.reflect.KProperty

class CSSafeWeakProperty<T>(
    parent: CSHasDestruct,
    value: T? = null,
    onChangeUnsafe: ((value: T?) -> Unit)? = null
) : CSPropertyBase<T?>(parent, onChangeUnsafe), CSSafeProperty<T?> {

    private val eventUnsafeChange = event<T?>()
    override fun onUnsafeChange(function: (T?) -> Unit): CSRegistration =
        eventUnsafeChange.listen(function)

    override fun getValue(thisRef: Any?, property: KProperty<*>): T? = value

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        this.value = value
    }

    private val _value = AtomicReference<WeakReference<T?>?>(null)

    override fun getAndSet(newValue: T?): T? {
        while (true) {
            val currentRef = _value.get()
            val oldValue = currentRef?.get()
            // if they’re already equal, still go through the CAS so that we consistently
            // return the oldValue, but we’ll still fire onValueChanged if CAS succeeds.
            val newRef = WeakReference<T?>(newValue)
            if (_value.compareAndSet(currentRef, newRef)) {
                // only fire if it actually changed under the hood
                onValueChanged(newValue)
                return oldValue
            }
            // otherwise, retry
        }
    }

    override fun compareAndSet(value: T?, newValue: T?): Boolean {
        while (true) {
            val currentRef = _value.get()
            val observed = currentRef?.get()
            if (observed != value) {
                // fast-fail if the wrapped value doesn’t match
                return false
            }
            // prepare the new weak ref
            val newRef = WeakReference<T?>(newValue)
            if (_value.compareAndSet(currentRef, newRef)) {
                onValueChanged(newValue)
                return true
            }
            // otherwise, another thread raced us; retry
        }
    }

    override fun value(newValue: T?, fire: Boolean) {
        //This was proposed by Gemini and ChaGpt after long conversation..
        while (true) {
            val current = _value.get()
            if (current?.get() == newValue) return
            val weakReference = WeakReference(newValue)
            if (_value.compareAndSet(current, weakReference)) {
                onValueChanged(newValue)
                return
            }
        }
    }

    init {
        value?.let { this.value = it }
    }

    @Volatile
    override var isChanged = false

    override fun fireChange() = value.let {
        onChange?.invoke(it)
        eventUnsafeChange.fire(it)
        onMain { eventChange.fire(it) }
    }

    override fun pause() {
        eventUnsafeChange.pause()
        super<CSPropertyBase>.pause()
    }

    override fun resume(fireChange: Boolean) {
        eventUnsafeChange.resume()
        super<CSPropertyBase>.resume(fireChange)
    }

    override var value: T?
        get() = _value.get()?.get()
        set(value) = this.value(value)

    companion object {
        fun <T> CSHasDestruct.safeWeakProperty(
            value: T? = null,
            onChangeUnsafe: ((value: T?) -> Unit)? = null
        ): CSProperty<T?> = CSSafeWeakProperty(this, value, onChangeUnsafe)
    }
}
