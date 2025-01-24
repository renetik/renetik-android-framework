package renetik.android.ui.extensions.view

import android.view.View
import android.view.View.OnAttachStateChangeListener
import android.view.View.OnLayoutChangeListener
import android.view.ViewTreeObserver.OnGlobalFocusChangeListener
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import kotlinx.coroutines.delay
import renetik.android.core.kotlin.primitives.isTrue
import renetik.android.core.lang.ArgFunc
import renetik.android.core.lang.Func
import renetik.android.core.lang.variable.toggle
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.property.CSProperty
import renetik.android.event.registration.CSHasChangeValue
import renetik.android.event.registration.CSHasRegistrations
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.CSRegistration.Companion.CSRegistration
import renetik.android.event.registration.action
import renetik.android.event.registration.launch
import renetik.android.event.registration.onChange
import renetik.android.event.registration.plus
import renetik.android.event.registration.start
import renetik.android.ui.R

fun View.onGlobalFocus(function: (View?, View?) -> Unit): CSRegistration {
    lateinit var registration: CSRegistration
    val listener = OnGlobalFocusChangeListener { old, new ->
        if (registration.isActive) function(old, new)
    }

    fun attach() = viewTreeObserver.addOnGlobalFocusChangeListener(listener)
    fun detach() = viewTreeObserver.removeOnGlobalFocusChangeListener(listener)

    val attachStateListener = object : OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(view: View) = attach()
        override fun onViewDetachedFromWindow(view: View) = detach()
    }
    addOnAttachStateChangeListener(attachStateListener)

    registration = CSRegistration(onResume = { if (isAttachedToWindow) attach() },
        onPause = { detach() },
        onCancel = { removeOnAttachStateChangeListener(attachStateListener) }).start()

    return registration
}

fun View.onGlobalLayout(function: (CSRegistration) -> Unit): CSRegistration {
    lateinit var registration: CSRegistration
    val listener =
        OnGlobalLayoutListener { if (registration.isActive) function(registration) }

    fun attach() = viewTreeObserver.addOnGlobalLayoutListener(listener)
    fun detach() = viewTreeObserver.removeOnGlobalLayoutListener(listener)
    val attachStateListener = object : OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(view: View) = attach()
        override fun onViewDetachedFromWindow(view: View) = detach()
    }
    addOnAttachStateChangeListener(attachStateListener)
    registration = CSRegistration(onResume = { if (isAttachedToWindow) attach() },
        onPause = { detach() },
        onCancel = { removeOnAttachStateChangeListener(attachStateListener) }).start()
    return registration
}

inline fun View.afterGlobalLayout(crossinline function: (View) -> Unit): CSRegistration =
    onGlobalLayout { it.cancel(); function(this) }

@Deprecated("Use simple version")
inline fun View.afterGlobalLayout(
    parent: CSHasRegistrations, crossinline function: (View) -> Unit
): CSRegistration {
    var registration: CSRegistration? = null
    return (parent + onGlobalLayout {
        if (it.isActive) function(this)
        registration?.cancel()
    }).also { registration = it }
}

inline fun View.onViewLayout(crossinline function: () -> Unit): CSRegistration {
    val listener = OnLayoutChangeListener { _, _, _, _, _, _, _, _, _ -> function() }
    return CSRegistration(onResume = { addOnLayoutChangeListener(listener) },
        onPause = { removeOnLayoutChangeListener(listener) }).start()
}

fun View.onBoundsChange(function: Func): CSRegistration {
    lateinit var registration: CSRegistration
    val listener = OnLayoutChangeListener { _, l, t, r, b, nl, nt, nr, nb ->
        if (l != nl || t != nt || r != nr || b != nb) function()
    }
    registration = CSRegistration(onResume = { addOnLayoutChangeListener(listener) },
        onPause = { removeOnLayoutChangeListener(listener) }).start()
    return registration
}

fun View.onHasSizeBoundsChange(function: Func): CSRegistration =
    onBoundsChange { if (hasSize) function() }

suspend fun View.waitForSize() {
    while (!hasSize) delay(20)
}

inline fun View.onHasSize(
    parent: CSHasRegistrations,
    crossinline function: (View) -> Unit
): CSRegistration? {
    if (!hasSize) {
        var registration: CSRegistration? = null
        return (parent + onHasSizeBoundsChange {
            registration?.cancel()
            function(this)
        }).also { registration = it }
    } else function(this)
    return null
}


inline fun View.onHasSize2(
    parent: CSHasRegistrations,
    crossinline function: (View) -> Unit
): CSRegistration? {
    if (!hasSize) return parent.launch {
        waitForSize()
        function(this)
    }
    function(this)
    return null
}


fun View.onScrollChange(function: (view: View) -> Unit): CSRegistration =
    eventScrollChange.listen(function)

val View.eventScrollChange
    get() = propertyWithTag(R.id.ViewEventOnScrollTag) {
        event<View>().also {
            setOnScrollChangeListener { _, _, _, _, _ ->
                it.fire(
                    this
                )
            }
        }
    }

fun View.disabledIf(property: CSHasChangeValue<Boolean>): CSRegistration =
    disabledIf(property) { it }

fun <T> View.disabledIf(
    property: CSHasChangeValue<T>, condition: (T) -> Boolean
): CSRegistration = property.action { disabledIf(condition(property.value)) }

fun <T, V> View.disabledIf(
    property1: CSHasChangeValue<T>, property2: CSHasChangeValue<V>,
    condition: (T, V) -> Boolean
): CSRegistration {
    fun update() = disabledIf(condition(property1.value, property2.value))
    update()
    return CSRegistration(property1.onChange(::update), property2.onChange(::update))
}

fun View.toggleSelected(property: CSProperty<Boolean>, timeout: Int? = null): CSRegistration {
    onClick(timeout) { property.toggle() }
    return selectedIf(property)
}

fun View.toggleAsActive(property: CSProperty<Boolean>, timeout: Int? = null): CSRegistration {
    onClick(timeout) { property.toggle() }
    return activeIf(property)
}


fun <T> View.selectIf(property: CSProperty<T>, value: T): CSRegistration {
    onClick { property.value = value }
    return selectedIf(property) { it == value }
}

inline fun <T> View.selectedIf(
    property: CSHasChangeValue<T>, crossinline condition: (T) -> Boolean
): CSRegistration = property.action { selected(condition(property.value)) }

fun View.selectedIf(property: CSHasChangeValue<Boolean>): CSRegistration =
    selectedIf(property) { it.isTrue }

fun <T> View.activateIf(property: CSProperty<T>, value: T): CSRegistration {
    onClick { property.value = value }
    return activeIf(property) { it == value }
}

inline fun <T> View.activeIf(
    property: CSHasChangeValue<T>, crossinline condition: (T) -> Boolean
): CSRegistration = property.action { active(condition(property.value)) }

fun View.activeIf(property: CSHasChangeValue<Boolean>): CSRegistration =
    activeIf(property) { it }

inline fun <T> View.activeIf(
    property1: CSHasChangeValue<T>,
    property2: CSHasChangeValue<*>,
    crossinline condition: (T) -> Boolean
): CSRegistration = activeIf(property1, property2) { first, _ -> condition(first) }

fun <T, V> View.activeIf(
    property1: CSHasChangeValue<T>, property2: CSHasChangeValue<V>,
    condition: (T, V) -> Boolean
): CSRegistration {
    fun update() = active(condition(property1.value, property2.value))
    update()
    return CSRegistration(property1.onChange(::update), property2.onChange(::update))
}

fun <T, V, X> View.activeIf(
    property1: CSHasChangeValue<T>,
    property2: CSHasChangeValue<V>,
    property3: CSHasChangeValue<X>,
    condition: (T, V, X) -> Boolean
): CSRegistration {
    fun update() = active(condition(property1.value, property2.value, property3.value))
    update()
    return CSRegistration(
        property1.onChange(::update), property2.onChange(::update),
        property3.onChange(::update)
    )
}

fun <T, V, X, Y> View.activeIf(
    property1: CSHasChangeValue<T>,
    property2: CSHasChangeValue<V>,
    property3: CSHasChangeValue<X>,
    property4: CSHasChangeValue<Y>,
    condition: (T, V, X, Y) -> Boolean
): CSRegistration {
    fun update() = active(
        condition(
            property1.value, property2.value, property3.value, property4.value
        )
    )
    update()
    return CSRegistration(
        property1.onChange(::update),
        property2.onChange(::update),
        property3.onChange(::update),
        property4.onChange(::update)
    )
}

inline fun <T> View.selectedIf(
    property1: CSHasChangeValue<T>,
    property2: CSHasChangeValue<*>,
    crossinline condition: (T) -> Boolean
): CSRegistration = selectedIf(property1, property2) { first, _ -> condition(first) }

fun <T, V> View.selectedIf(
    property1: CSHasChangeValue<T>, property2: CSHasChangeValue<V>,
    condition: (T, V) -> Boolean
): CSRegistration {
    fun update() = selected(condition(property1.value, property2.value))
    update()
    return CSRegistration(property1.onChange(::update), property2.onChange(::update))
}

fun <T, V, X> View.selectedIf(
    property1: CSHasChangeValue<T>,
    property2: CSHasChangeValue<V>,
    property3: CSHasChangeValue<X>,
    condition: (T, V, X) -> Boolean
): CSRegistration {
    fun update() = selected(condition(property1.value, property2.value, property3.value))
    update()
    return CSRegistration(
        property1.onChange(::update), property2.onChange(::update),
        property3.onChange(::update)
    )
}

fun <T, V, X, Y> View.selectedIf(
    property1: CSHasChangeValue<T>,
    property2: CSHasChangeValue<V>,
    property3: CSHasChangeValue<X>,
    property4: CSHasChangeValue<Y>,
    condition: (T, V, X, Y) -> Boolean
): CSRegistration {
    fun update() = selected(
        condition(
            property1.value, property2.value, property3.value, property4.value
        )
    )
    update()
    return CSRegistration(
        property1.onChange(::update),
        property2.onChange(::update),
        property3.onChange(::update),
        property4.onChange(::update)
    )
}

fun <T> View.pressedIf(
    property1: CSHasChangeValue<T>, property2: CSHasChangeValue<*>,
    condition: (T) -> Boolean
): CSRegistration = pressedIf(property1, property2) { first, _ -> condition(first) }

fun <T, V> View.pressedIf(
    property1: CSHasChangeValue<T>, property2: CSHasChangeValue<V>,
    condition: (T, V) -> Boolean
): CSRegistration {
    fun update() = pressed(condition(property1.value, property2.value))
    update()
    return CSRegistration(property1.onChange(::update), property2.onChange(::update))
}