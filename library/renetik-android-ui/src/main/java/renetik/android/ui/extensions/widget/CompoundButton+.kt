package renetik.android.ui.extensions.widget

import android.content.res.ColorStateList.valueOf
import android.view.View
import android.widget.CompoundButton
import androidx.annotation.ColorInt
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.property.CSProperty
import renetik.android.event.registration.CSHasChangeValue.Companion.action
import renetik.android.event.registration.CSHasChangeValue
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.CSRegistration.Companion.CSRegistration
import renetik.android.event.registration.onChange
import renetik.android.event.registration.paused
import renetik.android.ui.R
import renetik.android.ui.extensions.view.propertyWithTag

val CompoundButton.eventChange
    get() = propertyWithTag(R.id.ViewEventOnChangeTag) {
        event<View>().also { setOnCheckedChangeListener { _, _ -> it.fire(this) } }
    }

fun CompoundButton.onChange(function: (Boolean) -> Unit) =
    eventChange.listen { function(isChecked) }

fun CompoundButton.action(function: (Boolean) -> Unit): CSRegistration {
    function(isChecked)
    return onChange(function)
}

fun CompoundButton.buttonTint(@ColorInt value: Int?) = apply {
    buttonTintList = value?.let(::valueOf)
}

fun CompoundButton.checked(condition: Boolean = true) = apply { isChecked = condition }
fun CompoundButton.checkIf(condition: Boolean) = apply { isChecked = condition }
fun CompoundButton.checkIfNot(condition: Boolean) = apply { isChecked = !condition }
fun CompoundButton.setOn() = apply { isChecked = true }
fun CompoundButton.setOff() = apply { isChecked = false }

fun CompoundButton.checkIfNot(property: CSProperty<Boolean>): CSRegistration {
    lateinit var propertyRegistration: CSRegistration
    val buttonRegistration = onChange { propertyRegistration.paused { property.value(!isChecked) } }
    propertyRegistration = property.action { buttonRegistration.paused { checkIfNot(it) } }
    return CSRegistration(propertyRegistration, buttonRegistration)
}

fun CompoundButton.checkIf(property: CSProperty<Boolean>): CSRegistration {
    lateinit var propertyRegistration: CSRegistration
    val buttonRegistration = onChange { propertyRegistration.paused { property.value(isChecked) } }
    propertyRegistration = property.action { buttonRegistration.paused { checkIf(it) } }
    return CSRegistration(propertyRegistration, buttonRegistration)
}

fun CompoundButton.checkedIf(property: CSHasChangeValue<Boolean>): CSRegistration =
    property.action { checkIf(it) }

fun <T, V> CompoundButton.checkIf(
    property: CSProperty<T>, condition: (T) -> Boolean
): CSRegistration {
    return property.action { checkIf(condition(property.value)) }
}

fun <T> CompoundButton.checkIf(
    property1: CSProperty<T>, property2: CSProperty<*>, condition: (T) -> Boolean
)
    : CSRegistration = checkIf(property1, property2) { first, _ -> condition(first) }

fun <T, V> CompoundButton.checkIf(
    property1: CSProperty<T>, property2: CSProperty<V>,
    condition: (T, V) -> Boolean
): CSRegistration {
    fun update() = checkIf(condition(property1.value, property2.value))
    update()
    return CSRegistration(property1.onChange(::update), property2.onChange(::update))
}

inline fun <T> CompoundButton.checkIf(
    parent: CSHasChangeValue<T>,
    crossinline child: (T) -> CSProperty<Boolean>
): CSRegistration {
    var childRegistration: CSRegistration? = null
    val parentRegistration = parent.action {
        childRegistration?.cancel()
        childRegistration = checkIf(child(it))
    }
    return CSRegistration(isActive = true, onCancel = {
        parentRegistration.cancel()
        childRegistration?.cancel()
    })
}

@JvmName("checkedIfChildNullable")
inline fun <T> CompoundButton.checkIf(
    parent: CSHasChangeValue<T>,
    crossinline childNullable: (T) -> CSProperty<Boolean>?
): CSRegistration {
    var childRegistration: CSRegistration? = null
    val parentRegistration = parent.action {
        childRegistration?.cancel()
        childRegistration = childNullable(it)?.let { checkIf(it) }
        if (childRegistration == null) checkIf(false)
    }
    return CSRegistration(isActive = true, onCancel = {
        parentRegistration.cancel()
        childRegistration?.cancel()
    })
}
