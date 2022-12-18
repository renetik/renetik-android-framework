package renetik.android.ui.extensions.view

import android.view.View
import android.view.ViewTreeObserver.OnGlobalFocusChangeListener
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import renetik.android.core.kotlin.primitives.isFalse
import renetik.android.core.kotlin.primitives.isTrue
import renetik.android.core.lang.ArgFunc
import renetik.android.core.lang.Func
import renetik.android.core.lang.variable.CSWeakVariable
import renetik.android.core.lang.variable.CSWeakVariable.Companion.weak
import renetik.android.core.lang.variable.toggle
import renetik.android.core.lang.void
import renetik.android.event.CSEvent
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.property.CSProperty
import renetik.android.event.registration.*
import renetik.android.event.registration.CSRegistration.Companion.CSRegistration
import renetik.android.ui.R

fun View.onGlobalFocus(function: (View?, View?) -> Unit): CSRegistration {
    lateinit var registration: CSRegistration
    val listener = OnGlobalFocusChangeListener { old, new ->
        if (registration.isActive) function(old, new)
    }

    fun attach() = viewTreeObserver.addOnGlobalFocusChangeListener(listener)
    fun detach() = viewTreeObserver.removeOnGlobalFocusChangeListener(listener)

    val attachStateListener = object : View.OnAttachStateChangeListener {
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
    val listener = OnGlobalLayoutListener {
        if (registration.isActive) function(registration)
    }

    fun attach() = viewTreeObserver.addOnGlobalLayoutListener(listener)
    fun detach() = viewTreeObserver.removeOnGlobalLayoutListener(listener)

    val attachStateListener = object : View.OnAttachStateChangeListener {
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

inline fun View.onLayoutChange(crossinline function: () -> Unit): CSRegistration {
    val listener = View.OnLayoutChangeListener { _, _, _, _, _, _, _, _, _ -> function() }
    return CSRegistration(
        onResume = { addOnLayoutChangeListener(listener) },
        onPause = { removeOnLayoutChangeListener(listener) }).start()
}

fun View.onSizeChange(function: ArgFunc<CSRegistration>): CSRegistration {
    lateinit var registration: CSRegistration
    val listener = View.OnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
        function(registration)
    }
    registration = CSRegistration(onResume = { addOnLayoutChangeListener(listener) },
        onPause = { removeOnLayoutChangeListener(listener) }).start()
    return registration
}

fun View.onHasSizeChange(function: Func): CSRegistration =
    onSizeChange { if (hasSize) function() }

inline fun View.onHasSize(
    parent: CSHasRegistrations, crossinline function: (View) -> Unit): CSRegistration? {
    if (!hasSize) {
        val registration by weak(parent.register(onSizeChange {
            if (hasSize) {
                parent.cancel(it)
                function(this)
            }
        }))
        return CSRegistration { parent.cancel(registration) }
    } else function(this)
    return null
}

inline fun View.afterLayout(
    parent: CSHasRegistrations, crossinline function: (View) -> Unit): CSRegistration {
    val registration by weak(parent.register(onGlobalLayout {
        parent.cancel(it)
        function(this)
    }))
    return CSRegistration { parent.cancel(registration) }
}

fun View.onScrollChange(function: (view: View) -> Unit): CSRegistration =
    eventScrollChange.listen(function)

private val View.eventScrollChange
    get() = propertyWithTag(R.id.ViewEventOnScrollTag) {
        event<View>().also { setOnScrollChangeListener { _, _, _, _, _ -> it.fire(this) } }
    }

fun View.disabledIf(property: CSProperty<Boolean>): CSRegistration =
    disabledIf(property) { it }

fun <T> View.disabledIf(property: CSProperty<T>,
                        condition: (T) -> Boolean): CSRegistration {
    disabledIf(condition(property.value))
    return property.onChange { disabledIf(condition(property.value)) }
}

fun <T, V> View.disabledIf(property1: CSProperty<T>, property2: CSProperty<V>,
                           condition: (T, V) -> Boolean): CSRegistration {
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
    return activatedIf(property) { it.isTrue }
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
    property: CSProperty<T>, crossinline condition: (T) -> Boolean): CSRegistration {
    selected(condition(property.value))
    return property.onChange { selected(condition(property.value)) }
}

fun View.selectedIf(property: CSProperty<Boolean>): CSRegistration =
    selectedIf(property) { it.isTrue }

fun <T> View.activateIf(property: CSProperty<T>, value: T): CSRegistration {
    onClick { property.value = value }
    return activatedIf(property) { it == value }
}

inline fun <T> View.activatedIf(property: CSProperty<T>,
                                crossinline condition: (T) -> Boolean): CSRegistration {
    activated(condition(property.value))
    return property.onChange { activated(condition(property.value)) }
}

fun View.activatedIf(property: CSProperty<Boolean>): CSRegistration =
    activatedIf(property) { it }

inline fun <T> View.activatedIf(
    property1: CSProperty<T>, property2: CSProperty<*>,
    crossinline condition: (T) -> Boolean): CSRegistration =
    activatedIf(property1, property2) { first, _ -> condition(first) }

fun <T, V> View.activatedIf(property1: CSProperty<T>, property2: CSProperty<V>,
                            condition: (T, V) -> Boolean): CSRegistration {
    fun update() = activated(condition(property1.value, property2.value))
    update()
    return CSRegistration(property1.onChange(::update), property2.onChange(::update))
}

fun <T, V, X> View.activatedIf(
    property1: CSProperty<T>, property2: CSProperty<V>,
    property3: CSProperty<X>, condition: (T, V, X) -> Boolean): CSRegistration {
    fun update() = activated(condition(property1.value, property2.value, property3.value))
    update()
    return CSRegistration(property1.onChange(::update),
        property2.onChange(::update), property3.onChange(::update)
    )
}

fun <T, V, X, Y> View.activatedIf(
    property1: CSProperty<T>, property2: CSProperty<V>,
    property3: CSProperty<X>, property4: CSProperty<Y>,
    condition: (T, V, X, Y) -> Boolean): CSRegistration {
    fun update() = activated(condition(property1.value,
        property2.value, property3.value, property4.value))
    update()
    return CSRegistration(property1.onChange(::update), property2.onChange(::update),
        property3.onChange(::update), property4.onChange(::update))
}

inline fun <T> View.selectedIf(property1: CSProperty<T>, property2: CSProperty<*>,
                               crossinline condition: (T) -> Boolean): CSRegistration =
    selectedIf(property1, property2) { first, _ -> condition(first) }

fun <T, V> View.selectedIf(property1: CSProperty<T>, property2: CSProperty<V>,
                           condition: (T, V) -> Boolean): CSRegistration {
    fun update() = selected(condition(property1.value, property2.value))
    update()
    return CSRegistration(property1.onChange(::update), property2.onChange(::update))
}

fun <T, V, X> View.selectedIf(
    property1: CSProperty<T>, property2: CSProperty<V>,
    property3: CSProperty<X>, condition: (T, V, X) -> Boolean): CSRegistration {
    fun update() = selected(condition(property1.value, property2.value, property3.value))
    update()
    return CSRegistration(property1.onChange(::update),
        property2.onChange(::update), property3.onChange(::update))
}

fun <T, V, X, Y> View.selectedIf(
    property1: CSProperty<T>, property2: CSProperty<V>,
    property3: CSProperty<X>, property4: CSProperty<Y>,
    condition: (T, V, X, Y) -> Boolean): CSRegistration {
    fun update() = selected(condition(property1.value, property2.value,
        property3.value, property4.value))
    update()
    return CSRegistration(property1.onChange(::update), property2.onChange(::update),
        property3.onChange(::update), property4.onChange(::update)
    )
}

fun <T> View.pressedIf(property1: CSProperty<T>, property2: CSProperty<*>,
                       condition: (T) -> Boolean): CSRegistration =
    pressedIf(property1, property2) { first, _ -> condition(first) }

fun <T, V> View.pressedIf(property1: CSProperty<T>, property2: CSProperty<V>,
                          condition: (T, V) -> Boolean): CSRegistration {
    fun update() = pressedIf(condition(property1.value, property2.value))
    update()
    return CSRegistration(property1.onChange(::update), property2.onChange(::update))
}

//@Deprecated("Don't use")
//fun CSHasRegistrations.onHasSize2(
//    view: View, onHasSize: () -> Unit): CSRegistration? {
//    if (!view.hasSize) {
//        val registrations = CSRegistrationsList(this)
//        lateinit var laterEach: CSRegistration
//        val onGlobalLayout = registrations.register(view.onGlobalLayout {
//            if (view.hasSize) {
//                registrations.cancel(it)
//                registrations.cancel(laterEach)
//                onHasSize()
//            }
//        })
//        laterEach = registrations.register(laterEach(1) {
//            if (view.hasSize) {
//                registrations.cancel(it)
//                registrations.cancel(onGlobalLayout)
//                onHasSize()
//            }
//        })
//        return registrations
//    } else onHasSize()
//    return null
//}

