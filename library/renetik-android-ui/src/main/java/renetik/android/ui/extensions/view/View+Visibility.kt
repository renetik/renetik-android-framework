package renetik.android.ui.extensions.view

import android.view.View
import android.view.View.*
import androidx.appcompat.widget.ContentFrameLayout
import renetik.android.core.kotlin.primitives.isTrue
import renetik.android.event.property.CSProperty
import renetik.android.event.registration.CSRegistration
import renetik.android.ui.protocol.CSVisibility

fun <T : View> T.visible(animated: Boolean = false) = apply {
    if (animated) fadeIn() else {
        visibility = VISIBLE
        updateVisibility()
    }
}

fun <T : View> T.invisible(animated: Boolean = false) = apply {
    if (animated) fadeOut(invisible = true) else {
        visibility = INVISIBLE
        updateVisibility()
    }
}

fun <T : View> T.updateVisibility() {
    (tag as? CSVisibility)?.updateVisibility()
}

fun <T : View> T.gone(animated: Boolean = false) = apply {
    if (animated) fadeOut() else {
        visibility = GONE
        updateVisibility()
    }
}

val <T : View> T.isVisible get() = visibility == VISIBLE
val <T : View> T.isInvisible get() = visibility == INVISIBLE
val <T : View> T.isGone get() = visibility == GONE

fun <T : View> T.show(animated: Boolean = false) = apply { visible(animated) }
fun <T : View> T.hide(animated: Boolean = false) = apply { gone(animated) }

fun <T : View> T.visibleIf(condition: Boolean, animated: Boolean = false) = apply {
    if (condition) visible(animated) else invisible(animated)
}

fun <T : View> T.invisibleIf(condition: Boolean, animated: Boolean = false) =
    visibleIf(!condition, animated)

fun <T : View> T.shownIf(condition: Boolean, animated: Boolean = false) = apply {
    if (condition) show(animated) else gone(animated)
}

fun <T : View> T.goneIf(condition: Boolean, animated: Boolean = false) =
    shownIf(!condition, animated)

fun <T> View.shownIf(property: CSProperty<T>,
                     animated: Boolean = false, condition: (T) -> Boolean): CSRegistration {
    shownIf(condition(property.value))
    return property.onChange { shownIf(condition(property.value), animated) }
}

fun View.shownIf(property: CSProperty<Boolean>,
                 animated: Boolean = false): CSRegistration =
    shownIf(property, animated) { it }

fun View.shownIfNot(property: CSProperty<Boolean>,
                    animated: Boolean = false): CSRegistration =
    shownIf(property, animated) { !it }

fun <T> View.shownIf(property1: CSProperty<T>, property2: CSProperty<*>,
                     animated: Boolean = false, condition: (T) -> Boolean): CSRegistration =
    shownIf(property1, property2, animated) { first, _ -> condition(first) }

fun <T, V> View.shownIf(property1: CSProperty<T>, property2: CSProperty<V>,
                        animated: Boolean = false,
                        condition: (T, V) -> Boolean): CSRegistration {
    fun update(animated: Boolean) = shownIf(condition(property1.value, property2.value), animated)
    update(animated = false)
    return CSRegistration(
        property1.onChange { update(animated) },
        property2.onChange { update(animated) })
}

fun <T, V, X> View.shownIf(
    property1: CSProperty<T>, property2: CSProperty<V>, property3: CSProperty<X>,
    animated: Boolean = false, condition: (T, V, X) -> Boolean): CSRegistration {
    fun update(animated: Boolean) =
        shownIf(condition(property1.value, property2.value, property3.value), animated)
    update(animated = false)
    return CSRegistration(
        property1.onChange { update(animated) },
        property2.onChange { update(animated) },
        property3.onChange { update(animated) },
    )
}

fun View.goneIf(property1: CSProperty<Boolean>,
                property2: CSProperty<Boolean>,
                animated: Boolean = false): CSRegistration =
    goneIf(property1, property2, animated) { value1, value2 ->
        value1.isTrue || value2.isTrue
    }

fun <T, V> View.goneIf(property1: CSProperty<T>, property2: CSProperty<V>,
                       animated: Boolean = false,
                       condition: (T, V) -> Boolean): CSRegistration {
    fun update() = goneIf(condition(property1.value, property2.value), animated)
    update()
    return CSRegistration(
        property1.onChange { update() },
        property2.onChange { update() })
}

fun <T> View.goneIf(property: CSProperty<T>,
                    animated: Boolean = false, condition: (T) -> Boolean): CSRegistration {
    goneIf(condition(property.value))
    return property.onChange { goneIf(condition(property.value), animated) }
}

fun View.goneIf(property: CSProperty<Boolean>, animated: Boolean = false)
        : CSRegistration = goneIf(property, animated) { it }

fun <T> View.visibleIf(
    property1: CSProperty<T>, property2: CSProperty<*>,
    animated: Boolean = false, condition: (T) -> Boolean): CSRegistration =
    visibleIf(property1, property2, animated) { first, _ -> condition(first) }

fun <T, V> View.visibleIf(
    property1: CSProperty<T>, property2: CSProperty<V>,
    animated: Boolean = false, condition: (T, V) -> Boolean): CSRegistration {
    fun update() = visibleIf(condition(property1.value, property2.value), animated)
    update()
    return CSRegistration(property1.onChange { update() }, property2.onChange { update() })
}

fun <T> View.visibleIf(
    property: CSProperty<T>, animated: Boolean = false,
    condition: (T) -> Boolean): CSRegistration {
    visibleIf(condition(property.value))
    return property.onChange { visibleIf(condition(property.value), animated) }
}

fun View.visibleIf(
    property: CSProperty<Boolean>, animated: Boolean = false)
        : CSRegistration = visibleIf(property, animated) { it }

fun View.visibleIfNot(property: CSProperty<Boolean>, animated: Boolean = false)
        : CSRegistration = visibleIf(property, animated) { !it }

fun <T> View.invisibleIf(
    property: CSProperty<T>, animated: Boolean = false,
    condition: (T) -> Boolean): CSRegistration {
    invisibleIf(condition(property.value))
    return property.onChange { invisibleIf(condition(property.value), animated) }
}

fun View.invisibleIf(
    property: CSProperty<Boolean>, animated: Boolean = false)
        : CSRegistration = invisibleIf(property, animated) { it }

fun View.isShowing(): Boolean {
    if (!isVisible) return false
    var view: View = this
    while (true) {
        val parent = view.parent
        when {
            parent == null -> return false
            parent !is View -> return true
            parent is ContentFrameLayout -> return true
            !parent.isVisible -> return false
            (parent.tag as? CSVisibility)?.isVisible == true -> return true
            else -> view = parent
        }
    }
}