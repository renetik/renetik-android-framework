@file:OptIn(ExperimentalAtomicApi::class)

package renetik.android.event.change

import renetik.android.core.kotlin.primitives.isTrue
import renetik.android.core.lang.value.ifTrue
import renetik.android.event.property.CSSafeHasChangeValue
import renetik.android.event.registration.CSRegistration
import kotlin.concurrent.atomics.AtomicBoolean
import kotlin.concurrent.atomics.AtomicReference
import kotlin.concurrent.atomics.ExperimentalAtomicApi

@JvmName("BooleanAndCSSafeHasChangeValueBoolean")
infix fun Boolean.and(other: CSSafeHasChangeValue<Boolean>): CSSafeHasChangeValue<Boolean> =
    other.delegate(fromValue = { this && it })

@JvmName("CSSafeHasChangeValueBooleanAndBoolean")
infix fun CSSafeHasChangeValue<Boolean>.and(other: Boolean): CSSafeHasChangeValue<Boolean> =
    delegate(fromValue = { it && other })

@JvmName("CSSafeHasChangeValueBooleanAndCSSafeHasChangeValueBooleanLogical")
infix fun CSSafeHasChangeValue<Boolean>.and(
    other: CSSafeHasChangeValue<Boolean>
): CSSafeHasChangeValue<Boolean> =
    (this to other).delegate(fromValues = { first, second -> first && second })

@JvmName("CSSafeHasChangeValueBooleanAndCSSafeHasChangeValueBooleanNullable")
infix fun CSSafeHasChangeValue<Boolean>.and(
    other: CSSafeHasChangeValue<Boolean?>
): CSSafeHasChangeValue<Boolean> =
    (this to other).delegate(fromValues = { first, second -> first && second.isTrue })

@JvmName("CSHasChangeValueAndCSSafeHasChangeValueBoolean")
infix fun <T> CSHasChangeValue<T>.and(
    other: CSSafeHasChangeValue<Boolean>
): CSSafeHasChangeValue<T> {
    val first = this
    return object : CSSafeHasChangeValue<T> {
        override val value: T get() = first.value
        override fun onChange(function: (T) -> Unit) = CSRegistration(
            first.onChange { other.ifTrue { function(it) } },
            other.onTrue { function(value) },
        )

        override fun onUnsafeChange(function: (T) -> Unit): CSRegistration {
            val safeFirst = AtomicReference(first.value)
            return CSRegistration(
                first.onChange {
                    safeFirst.store(it)
                    other.ifTrue { function(it) }
                },
                other.onUnsafeTrue { function(safeFirst.load()) },
            )
        }
    }
}

@JvmName("CSSafeHasChangeValueAndCSSafeHasChangeValueBoolean")
infix fun <T> CSSafeHasChangeValue<T>.and(
    other: CSSafeHasChangeValue<Boolean>
): CSSafeHasChangeValue<T> {
    val first = this
    return object : CSSafeHasChangeValue<T> {
        override val value: T get() = first.value
        override fun onChange(function: (T) -> Unit) = CSRegistration(
            first.onChange { other.ifTrue { function(it) } },
            other.onTrue { function(value) },
        )

        override fun onUnsafeChange(function: (T) -> Unit) = CSRegistration(
            first.onUnsafeChange { other.ifTrue { function(it) } },
            other.onUnsafeTrue { function(value) },
        )
    }
}

@JvmName("CSSafeHasChangeValueAndCSHasChangeValueBoolean")
infix fun <T> CSSafeHasChangeValue<T>.and(
    other: CSHasChangeValue<Boolean>
): CSSafeHasChangeValue<T> {
    val first = this
    return object : CSSafeHasChangeValue<T> {
        override val value: T get() = first.value
        override fun onChange(function: (T) -> Unit) = CSRegistration(
            first.onChange { other.ifTrue { function(it) } },
            other.onTrue { function(value) },
        )

        override fun onUnsafeChange(function: (T) -> Unit): CSRegistration {
            val safeOther = AtomicBoolean(other.value)
            return CSRegistration(
                first.onUnsafeChange { if (safeOther.load()) function(it) },
                other.onChange {
                    safeOther.store(it)
                    if (it) function(value)
                },
            )
        }
    }
}

@JvmName("CSSafeHasChangeValueIfTrueCSSafeHasChangeValueBoolean")
infix fun <T> CSSafeHasChangeValue<T>.ifTrue(
    other: CSSafeHasChangeValue<Boolean>
): CSSafeHasChangeValue<T?> =
    (this to other).delegate(fromValues = { first, second ->
        if (second) first else null
    })

@JvmName("CSSafeHasChangeValueIfFalseCSSafeHasChangeValueBoolean")
infix fun <T> CSSafeHasChangeValue<T>.ifFalse(
    other: CSSafeHasChangeValue<Boolean>
): CSSafeHasChangeValue<T?> =
    (this to other).delegate(fromValues = { first, second ->
        if (!second) first else null
    })

@JvmName("CSSafeHasChangeValueBooleanOrCSSafeHasChangeValueBoolean")
infix fun CSSafeHasChangeValue<Boolean>.or(
    other: CSSafeHasChangeValue<Boolean>
): CSSafeHasChangeValue<Boolean> =
    (this to other).delegate(fromValues = { first, second -> first || second })

@JvmName("CSSafeHasChangeValueOrCSHasChange")
infix fun <T> CSSafeHasChangeValue<T>.or(second: CSHasChange<*>): CSSafeHasChangeValue<T> {
    val first = this
    return object : CSSafeHasChangeValue<T> {
        override val value: T get() = first.value
        override fun onChange(function: (T) -> Unit) = CSRegistration(
            first.onChange { function(it) },
            second.onChange { function(value) },
        )

        override fun onUnsafeChange(function: (T) -> Unit) = CSRegistration(
            first.onUnsafeChange { function(it) },
            second.onChange { function(value) },
        )
    }
}
