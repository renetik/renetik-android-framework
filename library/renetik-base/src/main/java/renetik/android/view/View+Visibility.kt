package renetik.android.view

import android.view.View
import android.view.View.*
import androidx.appcompat.widget.ContentFrameLayout
import renetik.android.framework.event.CSEventRegistration
import renetik.android.framework.event.CSMultiEventRegistration
import renetik.android.framework.event.CSVisibility
import renetik.android.framework.event.property.CSEventProperty
import renetik.android.primitives.isTrue

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

fun <T> View.shownIf(property: CSEventProperty<T>,
                     animated: Boolean = false, condition: (T) -> Boolean): CSEventRegistration {
    shownIf(condition(property.value))
    return property.onChange { shownIf(condition(property.value), animated) }
}

fun View.shownIf(property: CSEventProperty<Boolean>,
                 animated: Boolean = false) = shownIf(property, animated) { it }

fun <T> View.shownIf(property1: CSEventProperty<T>, property2: CSEventProperty<*>,
                     animated: Boolean = false, condition: (T) -> Boolean) =
    shownIf(property1, property2, animated) { first, _ -> condition(first) }

fun <T, V> View.shownIf(property1: CSEventProperty<T>, property2: CSEventProperty<V>,
                        animated: Boolean = false,
                        condition: (T, V) -> Boolean): CSEventRegistration {
    fun update(animated: Boolean) = shownIf(condition(property1.value, property2.value), animated)
    update(animated = false)
    return CSMultiEventRegistration(
        property1.onChange { update(animated) },
        property2.onChange { update(animated) })
}

fun View.goneIf(property1: CSEventProperty<Boolean>,
                property2: CSEventProperty<Boolean>,
                animated: Boolean = false): CSEventRegistration {
    return goneIf(property1, property2,
        animated) { value1, value2 -> value1.isTrue || value2.isTrue }
}

fun <T, V> View.goneIf(property1: CSEventProperty<T>, property2: CSEventProperty<V>,
                       animated: Boolean = false,
                       condition: (T, V) -> Boolean): CSEventRegistration {
    fun update() = goneIf(condition(property1.value, property2.value), animated)
    update()
    return CSMultiEventRegistration(
        property1.onChange { update() },
        property2.onChange { update() })
}

fun <T> View.goneIf(property: CSEventProperty<T>,
                    animated: Boolean = false, condition: (T) -> Boolean): CSEventRegistration {
    goneIf(condition(property.value))
    return property.onChange { goneIf(condition(property.value), animated) }
}

fun View.goneIf(property: CSEventProperty<Boolean>,
                animated: Boolean = false) = goneIf(property, animated) { it }

fun <T> View.visibleIf(property1: CSEventProperty<T>, property2: CSEventProperty<*>,
                       animated: Boolean = false, condition: (T) -> Boolean) =
    visibleIf(property1, property2, animated) { first, _ -> condition(first) }

fun <T, V> View.visibleIf(property1: CSEventProperty<T>, property2: CSEventProperty<V>,
                          animated: Boolean = false,
                          condition: (T, V) -> Boolean): CSEventRegistration {
    fun update() = visibleIf(condition(property1.value, property2.value), animated)
    update()
    return CSMultiEventRegistration(
        property1.onChange { update() },
        property2.onChange { update() })
}

fun <T> View.visibleIf(property: CSEventProperty<T>,
                       animated: Boolean = false, condition: (T) -> Boolean): CSEventRegistration {
    visibleIf(condition(property.value))
    return property.onChange { visibleIf(condition(property.value), animated) }
}

fun View.visibleIf(property: CSEventProperty<Boolean>,
                   animated: Boolean = false) = visibleIf(property, animated) { it }

fun <T> View.invisibleIf(property: CSEventProperty<T>,
                         animated: Boolean = false,
                         condition: (T) -> Boolean): CSEventRegistration {
    invisibleIf(condition(property.value))
    return property.onChange { invisibleIf(condition(property.value), animated) }
}

fun View.invisibleIf(property: CSEventProperty<Boolean>,
                     animated: Boolean = false) = invisibleIf(property, animated) { it }

// This had be done because isShown return false in on Resume
// for main activity view when created
// because it has not yet attached its DecorView.class to window
// DecorView is internal class so we cant identify it by class just className DecorView
// Other solution is to identify ContentFrameLayout instead as top view
// Previous simple "solution": view.parent?.parent?.parent?.parent != null
fun View.isShowing(): Boolean {
    if (!isVisible) return false
    var view: View = this
    while (true) {
        val parent = view.parent
        when {
            parent == null -> return false
            parent !is View -> return true
            parent is ContentFrameLayout -> return true
//            parent.className == "DecorView" -> return true
            !parent.isVisible -> return false
            else -> view = parent
        }
    }
}