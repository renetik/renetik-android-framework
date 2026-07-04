package renetik.android.event.change

import renetik.android.event.registration.CSHasRegistrations
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.CSRegistration.Companion.CSRegistration
import renetik.android.event.registration.isActive
import renetik.android.event.registration.registerTo

@JvmName("delegateChild")
fun <ParentValue, ChildValue> CSHasChangeValue<ParentValue>.delegate(
    parent: CSHasRegistrations? = null,
    fromValueChild: (ParentValue) -> CSHasChangeValue<ChildValue>,
): CSHasChangeValue<ChildValue> = let { property ->
    object : CSHasChangeValue<ChildValue> {
        override val value: ChildValue get() = fromValueChild(property.value).value
        override fun onChange(function: (ChildValue) -> Unit): CSRegistration {
            val value = CSValueFunction(this, value, function)
            var registration: CSRegistration? = null
            var childRegistration: CSRegistration? = null
            val parentRegistration = property.action { parentValue ->
                childRegistration?.cancel()
                val childItem = fromValueChild(parentValue)
                if (parent?.registrations.isActive && registration.isActive) childItem.also {
                    value(it.value)
                }
                childRegistration = childItem.onChange {
                    if (parent?.registrations.isActive && registration.isActive) value(it)
                }
            }
            return CSRegistration(isActive = true, onCancel = {
                parentRegistration.cancel()
                childRegistration?.cancel()
            }).also { registration = it }.registerTo(parent)
        }
    }
}

@JvmName("delegateChildren")
fun <Argument, Return> CSHasChangeValue<Argument>.delegateChange(
    parent: CSHasRegistrations? = null,
    fromValueChildList: (Argument) -> List<CSHasChange<out Return>>,
): CSHasChange<Return> = let { properties ->
    object : CSHasChange<Return> {
        override fun onChange(function: (Return) -> Unit): CSRegistration {
            var registrations: List<CSRegistration>? = null
            val parentRegistration = properties.action { parentValue ->
                registrations?.forEach(CSRegistration::cancel)
                val childList = fromValueChildList(parentValue)
                registrations = childList.map { it.onChange { function(it) } }
            }
            return CSRegistration(isActive = true, onCancel = {
                parentRegistration.cancel()
                registrations?.forEach(CSRegistration::cancel)
            }).registerTo(parent)
        }
    }
}

@JvmName("delegateChild")
fun <ParentValue, ChildValue> CSHasChangeValue<ParentValue>.delegateChange(
    parent: CSHasRegistrations? = null,
    fromValueChild: (ParentValue) -> CSHasChange<ChildValue>,
): CSHasChange<ChildValue> = let { property ->
    object : CSHasChange<ChildValue> {
        override fun onChange(function: (ChildValue) -> Unit): CSRegistration {
            var registration: CSRegistration? = null
            var childRegistration: CSRegistration? = null
            val parentRegistration = property.action { parentValue ->
                childRegistration?.cancel()
                val childItem = fromValueChild(parentValue)
                childRegistration = childItem.onChange {
                    if (parent?.registrations.isActive && registration.isActive) function(
                        it)
                }
            }
            return CSRegistration(isActive = true, onCancel = {
                parentRegistration.cancel()
                childRegistration?.cancel()
            }).also { registration = it }.registerTo(parent)
        }
    }
}

fun <ParentValue, ChildValue> CSHasChangeValue<ParentValue>.delegateChange(
    parent: CSHasRegistrations? = null,
    fromValueNullableChild: (ParentValue) -> CSHasChange<ChildValue>?,
): CSHasChange<ChildValue> = let { property ->
    object : CSHasChange<ChildValue> {
        override fun onChange(function: (ChildValue) -> Unit): CSRegistration {
            var registration: CSRegistration? = null
            var childRegistration: CSRegistration? = null
            val parentRegistration = property.action { parentValue ->
                childRegistration?.cancel()
                val childItem = fromValueNullableChild(parentValue)
                childRegistration = childItem?.onChange {
                    if (parent?.registrations.isActive && registration.isActive) function(
                        it)
                }
            }
            return CSRegistration(isActive = true, onCancel = {
                parentRegistration.cancel()
                childRegistration?.cancel()
            }).also { registration = it }.registerTo(parent)
        }
    }
}

@JvmName("delegateNullable")
fun <ParentValue, ChildValue> CSHasChangeValue<ParentValue>.delegate(
    parent: CSHasRegistrations? = null,
    fromValueNullableChild: (ParentValue) -> CSHasChangeValue<ChildValue>?,
): CSHasChangeValue<ChildValue?> = let { property ->
    object : CSHasChangeValue<ChildValue?> {
        override val value: ChildValue? get() = fromValueNullableChild(property.value)?.value
        override fun onChange(function: (ChildValue?) -> Unit): CSRegistration {
            val value = CSValueFunction(this, value, function)
            var registration: CSRegistration? = null
            var childRegistration: CSRegistration? = null
            val parentRegistration = property.action { parentValue ->
                childRegistration?.cancel()
                val childItem = fromValueNullableChild(parentValue)
                if (parent?.registrations.isActive && registration.isActive) childItem.also {
                    value(it?.value)
                }
                childRegistration = childItem?.onChange {
                    if (parent?.registrations.isActive && registration.isActive) value.invoke(
                        it)
                }
            }
            return CSRegistration(isActive = true, onCancel = {
                parentRegistration.cancel()
                childRegistration?.cancel()
            }).also { registration = it }.registerTo(parent)
        }
    }
}
