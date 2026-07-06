package renetik.android.event.property

import renetik.android.core.kotlin.notImplemented
import renetik.android.core.lang.value.CSValue
import renetik.android.core.lang.variable.assign
import renetik.android.event.change.CSSuspendHasChange
import renetik.android.event.change.CSSuspendHasChangeValue
import renetik.android.event.change.onChangeLaunch
import renetik.android.event.registration.CSHasRegistrations
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.paused
import renetik.android.event.registration.plus

interface CSSuspendProperty<T> : CSValue<T>, CSSuspendHasChange<T>, CSSuspendHasChangeValue<T> {
    companion object {
        suspend inline infix fun <T> CSSuspendProperty<T>.assign(other: T) = value(other)
        suspend inline infix fun <T> CSSuspendProperty<T>.assign(other: CSValue<T>) =
            value(other.value)

        fun <T> suspendProperty(
            value: T, onChange: suspend ((value: T) -> Unit) = {}
        ) = CSSuspendPropertyImpl(value, onChange)

        fun <T> CSProperty<T>.suspended(parent: CSHasRegistrations): CSSuspendProperty<T> {
            val property = suspendProperty(value)
            lateinit var propertyOnChange: CSRegistration
            val thisOnChange: CSRegistration = onChangeLaunch {
                propertyOnChange.paused { property assign it }
            }
            propertyOnChange = property.onChange {
                thisOnChange.paused { this assign it }
            }
            parent + CSRegistration(thisOnChange, propertyOnChange)
            return property
        }

    }

    suspend fun value(newValue: T, fire: Boolean = true)
    suspend fun fireChange()
    fun pause(): Unit = notImplemented()
    suspend fun resume(fireChange: Boolean = true): Unit = notImplemented()
}