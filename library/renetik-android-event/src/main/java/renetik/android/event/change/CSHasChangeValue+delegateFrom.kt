package renetik.android.event.change

import renetik.android.core.lang.tuples.CSQuadruple
import renetik.android.event.registration.CSHasRegistrations
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.isActive
import renetik.android.event.registration.registerTo

fun <T, Return> CSHasChangeValue<T>.delegate(
    parent: CSHasRegistrations? = null,
    fromValue: (T) -> Return,
): CSHasChangeValue<Return> = let { property ->
    object : CSHasChangeValue<Return> {
        override val value: Return get() = fromValue(property.value)
        override fun onChange(function: (Return) -> Unit): CSRegistration {
            val value = CSValueFunction(this, value, function)
            return property.onChange {
                if (parent?.registrations.isActive) value(fromValue(it))
            }.registerTo(parent)
        }
    }
}

fun <Argument, Return> List<CSHasChangeValue<Argument>>.delegate(
    parent: CSHasRegistrations? = null,
    fromValueList: (List<Argument>) -> Return,
): CSHasChangeValue<Return> = let { properties ->
    object : CSHasChangeValue<Return> {
        override val value: Return get() = fromValueList(properties.map { it.value })
        override fun onChange(function: (Return) -> Unit): CSRegistration {
            val value = CSValueFunction(this, value, function)
            return properties.onChange { list ->
                if (parent?.registrations.isActive) value(fromValueList(list))
            }.registerTo(parent)
        }
    }
}

fun <T, V, Return>
        Pair<CSHasChangeValue<T>,
                CSHasChangeValue<V>>.delegate(
    parent: CSHasRegistrations? = null,
    fromValues: (T, V) -> Return,
): CSHasChangeValue<Return> = object : CSHasChangeValue<Return> {
    override val value: Return get() = fromValues(first.value, second.value)
    override fun onChange(function: (Return) -> Unit): CSRegistration {
        val value = CSValueFunction(this, value, function)
        return CSRegistration(
            first.onChange {
                if (parent?.registrations.isActive)
                    value(fromValues(it, second.value))
            },
            second.onChange {
                if (parent?.registrations.isActive)
                    value(fromValues(first.value, it))
            },
        ).registerTo(parent)
    }
}

fun <T, V, K, Return>
        Triple<CSHasChangeValue<T>, CSHasChangeValue<V>,
                CSHasChangeValue<K>>.delegate(
    parent: CSHasRegistrations? = null,
    fromValues: (T, V, K) -> Return,
): CSHasChangeValue<Return> = object : CSHasChangeValue<Return> {
    override val value: Return
        get() = fromValues(first.value, second.value, third.value)

    override fun onChange(function: (Return) -> Unit): CSRegistration {
        val value = CSValueFunction(this, value, function)
        return CSRegistration(first.onChange {
            if (parent?.registrations.isActive) value(fromValues(it, second.value, third.value))
        }, second.onChange {
            if (parent?.registrations.isActive) value(fromValues(first.value, it, third.value))
        }, third.onChange {
            if (parent?.registrations.isActive) value(fromValues(first.value, second.value, it))
        }).registerTo(parent)
    }
}

fun <T, V, K, L, Return> CSQuadruple<CSHasChangeValue<T>,
        CSHasChangeValue<V>, CSHasChangeValue<K>,
        CSHasChangeValue<L>>.delegate(
    parent: CSHasRegistrations? = null,
    fromValues: (T, V, K, L) -> Return,
): CSHasChangeValue<Return> = object : CSHasChangeValue<Return> {
    override val value: Return
        get() = fromValues(first.value, second.value, third.value, fourth.value)

    override fun onChange(function: (Return) -> Unit): CSRegistration {
        val value = CSValueFunction(this, value, function)
        return CSRegistration(first.onChange {
            if (parent?.registrations.isActive)
                value(fromValues(it, second.value, third.value, fourth.value))
        }, second.onChange {
            if (parent?.registrations.isActive)
                value(fromValues(first.value, it, third.value, fourth.value))
        }, third.onChange {
            if (parent?.registrations.isActive)
                value(fromValues(first.value, second.value, it, fourth.value))
        }, fourth.onChange {
            if (parent?.registrations.isActive)
                value(fromValues(first.value, second.value, third.value, it))
        }).registerTo(parent)
    }
}
