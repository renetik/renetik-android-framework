package renetik.android.event.change

import renetik.android.event.property.CSSafeHasChangeValue
import renetik.android.event.registration.CSHasRegistrations
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.isActive
import renetik.android.event.registration.registerTo

fun <T> CSSafeHasChangeValue<T>.delegate(
    parent: CSHasRegistrations? = null,
): CSSafeHasChangeValue<T> = delegate(parent, fromValue = { it })

fun <T, Return> CSSafeHasChangeValue<T>.delegate(
    parent: CSHasRegistrations? = null,
    fromValue: (T) -> Return,
): CSSafeHasChangeValue<Return> = let { source ->
    object : CSSafeHasChangeValue<Return> {
        override val value: Return get() = fromValue(source.value)

        override fun onChange(function: (Return) -> Unit): CSRegistration {
            val value = CSValueFunction(this, value, function)
            return source.onChange {
                if (parent?.registrations.isActive) value.update { fromValue(source.value) }
            }.registerTo(parent)
        }

        override fun onUnsafeChange(function: (Return) -> Unit): CSRegistration {
            val value = CSValueFunction(this, value, function)
            return source.onUnsafeChange {
                if (parent?.registrations.isActive) value.update { fromValue(source.value) }
            }.registerTo(parent)
        }
    }
}

@JvmName("safePairDelegate")
fun <T, V, Return> Pair<CSSafeHasChangeValue<T>, CSSafeHasChangeValue<V>>.delegate(
    parent: CSHasRegistrations? = null,
    fromValues: (T, V) -> Return,
): CSSafeHasChangeValue<Return> = object : CSSafeHasChangeValue<Return> {
    override val value: Return get() = fromValues(first.value, second.value)

    override fun onChange(function: (Return) -> Unit): CSRegistration {
        val value = CSValueFunction(this, value, function)
        return CSRegistration(
            first.onChange {
                if (parent?.registrations.isActive) value.update {
                    fromValues(first.value, second.value)
                }
            },
            second.onChange {
                if (parent?.registrations.isActive) value.update {
                    fromValues(first.value, second.value)
                }
            },
        ).registerTo(parent)
    }

    override fun onUnsafeChange(function: (Return) -> Unit): CSRegistration {
        val value = CSValueFunction(this, value, function)
        return CSRegistration(
            first.onUnsafeChange {
                if (parent?.registrations.isActive) value.update {
                    fromValues(first.value, second.value)
                }
            },
            second.onUnsafeChange {
                if (parent?.registrations.isActive) value.update {
                    fromValues(first.value, second.value)
                }
            },
        ).registerTo(parent)
    }
}
