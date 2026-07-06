package renetik.android.event.property

import renetik.android.core.lang.atomic.CSAtomic.Companion.atomic
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.lifecycle.CSHasDestruct
import java.util.concurrent.atomic.AtomicReference
import kotlin.reflect.KProperty

class CSSafePropertyImpl<T>(
    parent: CSHasDestruct,
    value: T, onChangeUnsafe: ((value: T) -> Unit)? = null
) : CSPropertyBase<T>(parent, onChangeUnsafe), CSSafeProperty<T> {

    init {
        eventChange.onMain(parent)
    }

    private val eventUnsafeChange = event<T>()
    override fun onUnsafeChange(function: (T) -> Unit) =
        eventUnsafeChange.listen(function)

    private val field = AtomicReference(value)
    override fun getValue(thisRef: Any?, property: KProperty<*>): T = value
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) = value(value)

    override fun getAndSet(newValue: T): T {
        val previous = field.getAndSet(newValue)
        if (previous != newValue) onValueChanged(newValue)
        return previous
    }

    override fun compareAndSet(value: T, newValue: T): Boolean {
        val isSet = field.compareAndSet(value, newValue)
        if (isSet) onValueChanged(newValue)
        return isSet
    }

    override fun value(newValue: T, fire: Boolean) {
        if (field.getAndSet(newValue) != newValue)
            onValueChanged(newValue, fire)
    }

    @Volatile override var isChanged = false

    override fun fireChange() {
        val actualValue = value
        onChange?.invoke(actualValue)
        eventUnsafeChange.fire(actualValue)
        eventChange.fire(actualValue)
    }

    override fun pause() {
        eventUnsafeChange.pause()
        super<CSPropertyBase>.pause()
    }

    override fun resume(fireChange: Boolean) {
        eventUnsafeChange.resume()
        super<CSPropertyBase>.resume(fireChange)
    }

    override var value: T
        get() = this.field.get()
        set(value) = value(value)

    companion object {
        fun <T> CSHasDestruct.safeProperty(
            value: T, onChangeUnsafe: ((value: T) -> Unit)? = null
        ) = CSSafePropertyImpl(this, value, onChangeUnsafe)

        fun <T> CSHasDestruct.safeProperty(
            value: T, onChangeUnsafe: ((previous: T, current: T) -> Unit)
        ): CSSafePropertyImpl<T> {
            var previous by atomic(value)
            return CSSafePropertyImpl(this, value) {
                onChangeUnsafe(previous, it)
                previous = it
            }
        }
    }
}
