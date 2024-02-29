package renetik.android.ui.extensions.view

import android.view.View
import android.view.View.OnAttachStateChangeListener
import android.view.View.OnLayoutChangeListener
import android.view.ViewTreeObserver.OnGlobalFocusChangeListener
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import renetik.android.core.kotlin.primitives.isFalse
import renetik.android.core.kotlin.primitives.isTrue
import renetik.android.core.lang.ArgFunc
import renetik.android.core.lang.Func
import renetik.android.core.lang.variable.CSWeakVariable.Companion.weak
import renetik.android.core.lang.variable.toggle
import renetik.android.core.lang.void
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.property.CSProperty
import renetik.android.event.registration.*
import renetik.android.event.registration.CSHasChangeValue.Companion.action
import renetik.android.event.registration.CSRegistration.Companion.CSRegistration
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

    registration = CSRegistration(
        onResume = { if (isAttachedToWindow) attach() },
        onPause = { detach() },
        onCancel = { removeOnAttachStateChangeListener(attachStateListener) }
    ).start()

    return registration
}

fun View.onGlobalLayout(function: (CSRegistration) -> void): CSRegistration {
    lateinit var registration: CSRegistration
    val listener = OnGlobalLayoutListener { if (registration.isActive) function(registration) }
    fun attach() = viewTreeObserver.addOnGlobalLayoutListener(listener)
    fun detach() = viewTreeObserver.removeOnGlobalLayoutListener(listener)
    val attachStateListener = object : OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(view: View) = attach()
        override fun onViewDetachedFromWindow(view: View) = detach()
    }
    addOnAttachStateChangeListener(attachStateListener)
    registration = CSRegistration(
        onResume = { if (isAttachedToWindow) attach() },
        onPause = { detach() },
        onCancel = { removeOnAttachStateChangeListener(attachStateListener) }
    ).start()
    return registration
}

inline fun View.afterGlobalLayout(crossinline function: (View) -> Unit): CSRegistration =
    onGlobalLayout { it.cancel(); function(this) }

@Deprecated("Use simple version")
inline fun View.afterGlobalLayout(
    parent: CSHasRegistrations, crossinline function: (View) -> Unit
): CSRegistration {
    val registration by weak(parent + onGlobalLayout {
        parent.cancel(it)
        function(this)
    })
    return CSRegistration { parent.cancel(registration) }
}

inline fun View.onViewLayout(crossinline function: () -> Unit): CSRegistration {
    val listener = OnLayoutChangeListener { _, _, _, _, _, _, _, _, _ -> function() }
    return CSRegistration(
        onResume = { addOnLayoutChangeListener(listener) },
        onPause = { removeOnLayoutChangeListener(listener) }).start()
}

fun View.onBoundsChange(function: ArgFunc<CSRegistration>): CSRegistration {
    lateinit var registration: CSRegistration
    val listener = OnLayoutChangeListener { _, l, t, r, b, nl, nt, nr, nb ->
        if (l != nl || t != nt || r != nr || b != nb) function(registration)
    }
    registration = CSRegistration(onResume = { addOnLayoutChangeListener(listener) },
        onPause = { removeOnLayoutChangeListener(listener) }).start()
    return registration
}

fun View.onHasSizeBoundsChange(function: Func): CSRegistration =
    onBoundsChange { if (hasSize) function() }

inline fun View.onHasSize(
    parent: CSHasRegistrations, crossinline function: (View) -> Unit
): CSRegistration? {
    if (!hasSize) {
        val registration by weak(parent + onBoundsChange {
            if (hasSize) {
                parent.cancel(it)
                function(this)
            }
        })
        return CSRegistration { parent.cancel(registration) }
    } else function(this)
    return null
}

fun View.onScrollChange(function: (view: View) -> Unit): CSRegistration =
    eventScrollChange.listen(function)

private val View.eventScrollChange
    get() = propertyWithTag(R.id.ViewEventOnScrollTag) {
        event<View>().also { setOnScrollChangeListener { _, _, _, _, _ -> it.fire(this) } }
    }

fun View.disabledIf(property: CSHasChangeValue<Boolean>): CSRegistration =
    disabledIf(property) { it }

fun <T> View.disabledIf(
    property: CSHasChangeValue<T>,
    condition: (T) -> Boolean
): CSRegistration = property.action { disabledIf(condition(property.value)) }

fun <T, V> View.disabledIf(
    property1: CSHasChangeValue<T>, property2: CSHasChangeValue<V>,
    condition: (T, V) -> Boolean
): CSRegistration {
    fun update() = disabledIf(condition(property1.value, property2.value))
    update()
    return CSRegistration(property1.onChange(::update), property2.onChange(::update))
}

fun View.toggleSelectedAsTrue(property: CSProperty<Boolean>): CSRegistration {
    onClick { property.toggle() }
    return selectedIf(property) { it.isTrue }
}

fun View.toggleActiveAsTrue(property: CSProperty<Boolean>): CSRegistration {
    onClick { property.toggle() }
    return activeIf(property) { it.isTrue }
}

fun View.toggleAsFalse(property: CSProperty<Boolean>): CSRegistration {
    onClick { property.toggle() }
    return selectedIf(property) { it.isFalse }
}

fun <T> View.selectIf(property: CSProperty<T>, value: T): CSRegistration {
    onClick { property.value = value }
    return selectedIf(property) { it == value }
}

inline fun <T> View.selectedIf(
    property: CSHasChangeValue<T>, crossinline condition: (T) -> Boolean
): CSRegistration {
    selected(condition(property.value))
    return property.onChange { selected(condition(property.value)) }
}

fun View.selectedIf(property: CSHasChangeValue<Boolean>): CSRegistration =
    selectedIf(property) { it.isTrue }

fun <T> View.activateIf(property: CSProperty<T>, value: T): CSRegistration {
    onClick { property.value = value }
    return activeIf(property) { it == value }
}

inline fun <T> View.activeIf(
    property: CSHasChangeValue<T>,
    crossinline condition: (T) -> Boolean
): CSRegistration {
    activated(condition(property.value))
    return property.onChange { activated(condition(property.value)) }
}

fun View.activeIf(property: CSHasChangeValue<Boolean>): CSRegistration =
    activeIf(property) { it }

inline fun <T> View.activeIf(
    property1: CSHasChangeValue<T>, property2: CSHasChangeValue<*>,
    crossinline condition: (T) -> Boolean
): CSRegistration =
    activeIf(property1, property2) { first, _ -> condition(first) }

fun <T, V> View.activeIf(
    property1: CSHasChangeValue<T>, property2: CSHasChangeValue<V>,
    condition: (T, V) -> Boolean
): CSRegistration {
    fun update() = activated(condition(property1.value, property2.value))
    update()
    return CSRegistration(property1.onChange(::update), property2.onChange(::update))
}

fun <T, V, X> View.activeIf(
    property1: CSHasChangeValue<T>, property2: CSHasChangeValue<V>,
    property3: CSHasChangeValue<X>, condition: (T, V, X) -> Boolean
): CSRegistration {
    fun update() = activated(condition(property1.value, property2.value, property3.value))
    update()
    return CSRegistration(
        property1.onChange(::update),
        property2.onChange(::update), property3.onChange(::update)
    )
}

fun <T, V, X, Y> View.activeIf(
    property1: CSHasChangeValue<T>, property2: CSHasChangeValue<V>,
    property3: CSHasChangeValue<X>, property4: CSHasChangeValue<Y>,
    condition: (T, V, X, Y) -> Boolean
): CSRegistration {
    fun update() = activated(
        condition(
            property1.value,
            property2.value, property3.value, property4.value
        )
    )
    update()
    return CSRegistration(
        property1.onChange(::update), property2.onChange(::update),
        property3.onChange(::update), property4.onChange(::update)
    )
}

inline fun <T> View.selectedIf(
    property1: CSHasChangeValue<T>, property2: CSHasChangeValue<*>,
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
    property1: CSHasChangeValue<T>, property2: CSHasChangeValue<V>,
    property3: CSHasChangeValue<X>, condition: (T, V, X) -> Boolean
): CSRegistration {
    fun update() = selected(condition(property1.value, property2.value, property3.value))
    update()
    return CSRegistration(
        property1.onChange(::update),
        property2.onChange(::update), property3.onChange(::update)
    )
}

fun <T, V, X, Y> View.selectedIf(
    property1: CSHasChangeValue<T>, property2: CSHasChangeValue<V>,
    property3: CSHasChangeValue<X>, property4: CSHasChangeValue<Y>,
    condition: (T, V, X, Y) -> Boolean
): CSRegistration {
    fun update() = selected(
        condition(
            property1.value, property2.value,
            property3.value, property4.value
        )
    )
    update()
    return CSRegistration(
        property1.onChange(::update), property2.onChange(::update),
        property3.onChange(::update), property4.onChange(::update)
    )
}

fun <T> View.pressedIf(
    property1: CSHasChangeValue<T>, property2: CSHasChangeValue<*>,
    condition: (T) -> Boolean
): CSRegistration =
    pressedIf(property1, property2) { first, _ -> condition(first) }

fun <T, V> View.pressedIf(
    property1: CSHasChangeValue<T>, property2: CSHasChangeValue<V>,
    condition: (T, V) -> Boolean
): CSRegistration {
    fun update() = pressed(condition(property1.value, property2.value))
    update()
    return CSRegistration(property1.onChange(::update), property2.onChange(::update))
}