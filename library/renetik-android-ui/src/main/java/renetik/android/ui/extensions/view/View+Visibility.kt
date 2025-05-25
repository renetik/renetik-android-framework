@file:Suppress("NOTHING_TO_INLINE")

package renetik.android.ui.extensions.view

import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import renetik.android.core.kotlin.primitives.isTrue
import renetik.android.core.lang.value.isTrue
import renetik.android.event.registration.CSHasChangeValue
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.onValue
import renetik.android.ui.protocol.CSViewInterface
import renetik.android.ui.protocol.CSVisibility

fun <T : View> T.updateVisibility() {
    (tag as? CSVisibility)?.updateVisibility()
}

val <T : View> T.isVisible get() = visibility == VISIBLE
val <T : View> T.isInvisible get() = visibility == INVISIBLE
val <T : View> T.isGone get() = visibility == GONE

fun <T : View> T.visible(
    visible: Boolean = true, animated: Boolean = false,
) = apply {
    if (visible) {
        if (animated) fadeIn() else
            if (visibility != VISIBLE) {
                visibility = VISIBLE
                updateVisibility()
            }
    } else {
        if (animated) fadeOut(invisible = true) else
            if (visibility != INVISIBLE) {
                visibility = INVISIBLE
                updateVisibility()
            }
    }
}

fun <T : View> T.invisible(
    invisible: Boolean = true, animated: Boolean = false,
) = visible(visible = !invisible, animated)

inline fun <T : View> T.showIf(
    show: Boolean, animated: Boolean = false,
) = show(show, animated)

fun <T : View> T.show(
    show: Boolean = true, animated: Boolean = false,
) = apply {
    if (show) {
        if (animated) fadeIn() else
            if (visibility != VISIBLE) {
                visibility = VISIBLE
                updateVisibility()
            }
    } else {
        if (animated) fadeOut() else
            if (visibility != GONE) {
                visibility = GONE
                updateVisibility()
            }
    }
}

fun <T : View> T.gone(
    gone: Boolean = true, animated: Boolean = false,
): T = show(!gone, animated)

fun <T> View.shownIf(
    property: CSHasChangeValue<T>, animated: Boolean = false,
    condition: (T) -> Boolean,
): CSRegistration {
    show(condition(property.value))
    return property.onChange { show(condition(property.value), animated) }
}

fun View.shownIf(
    property: CSHasChangeValue<Boolean>,
    animated: Boolean = false,
): CSRegistration = shownIf(property, animated) { it }

fun View.shownIfNot(
    property: CSHasChangeValue<Boolean>,
    animated: Boolean = false,
): CSRegistration = shownIf(property, animated) { !it }

fun <T> View.shownIf(
    property1: CSHasChangeValue<T>, property2: CSHasChangeValue<*>,
    animated: Boolean = false, condition: (T) -> Boolean,
): CSRegistration =
    shownIf(property1, property2, animated) { first, _ -> condition(first) }

fun <T, V> View.shownIf(
    property1: CSHasChangeValue<T>, property2: CSHasChangeValue<V>,
    animated: Boolean = false,
    condition: (T, V) -> Boolean,
): CSRegistration {
    fun update(animated: Boolean) =
        show(condition(property1.value, property2.value), animated)
    update(animated = false)
    return CSRegistration(
        property1.onChange { update(animated) },
        property2.onChange { update(animated) }
    )
}

fun <T, V, X> View.shownIf(
    property1: CSHasChangeValue<T>,
    property2: CSHasChangeValue<V>,
    property3: CSHasChangeValue<X>,
    animated: Boolean = false,
    condition: (T, V, X) -> Boolean,
): CSRegistration {
    fun update(animated: Boolean) =
        show(condition(property1.value, property2.value, property3.value), animated)
    update(animated = false)
    return CSRegistration(
        property1.onChange { update(animated) },
        property2.onChange { update(animated) },
        property3.onChange { update(animated) },
    )
}

fun View.goneIf(
    property1: CSHasChangeValue<Boolean>,
    property2: CSHasChangeValue<Boolean>,
    animated: Boolean = false,
): CSRegistration = goneIf(property1, property2, animated) { value1, value2 ->
    value1.isTrue || value2.isTrue
}

fun <T, V> View.goneIf(
    property1: CSHasChangeValue<T>,
    property2: CSHasChangeValue<V>,
    animated: Boolean = false,
    condition: (T, V) -> Boolean,
): CSRegistration {
    fun update() = gone(condition(property1.value, property2.value), animated)
    update()
    return CSRegistration(
        property1.onChange { update() },
        property2.onChange { update() }
    )
}

fun <T> View.goneIf(
    property: CSHasChangeValue<T>,
    animated: Boolean = false, condition: (T) -> Boolean,
): CSRegistration {
    property.onValue { gone(condition(it)) }
    return property.onChange { gone(condition(property.value), animated) }
}

fun View.goneIf(
    property: CSHasChangeValue<Boolean>, animated: Boolean = false,
): CSRegistration = goneIf(property, animated) { it }

fun View.goneIfNot(
    property: CSHasChangeValue<Boolean>, animated: Boolean = false,
): CSRegistration = goneIf(property, animated) { !it }

fun View.visibleIf(
    property: CSHasChangeValue<Boolean>, animated: Boolean = false,
): CSRegistration = visibleIf(property, animated) { it }

fun View.visibleIfNot(
    property: CSHasChangeValue<Boolean>, animated: Boolean = false,
): CSRegistration = visibleIf(property, animated) { !it }

fun <T> View.visibleIf(
    property1: CSHasChangeValue<T>, property2: CSHasChangeValue<*>,
    animated: Boolean = false, condition: (T) -> Boolean,
): CSRegistration =
    visibleIf(property1, property2, animated) { first, _ -> condition(first) }

fun <T, V> View.visibleIf(
    property1: CSHasChangeValue<T>, property2: CSHasChangeValue<V>,
    animated: Boolean = false, condition: (T, V) -> Boolean,
): CSRegistration {
    fun update() = visible(condition(property1.value, property2.value), animated)
    update()
    return CSRegistration(property1.onChange { update() }, property2.onChange { update() })
}

fun <T, V> View.invisibleIf(
    property1: CSHasChangeValue<T>, property2: CSHasChangeValue<V>,
    animated: Boolean = false, condition: (T, V) -> Boolean,
): CSRegistration {
    fun update() = invisible(condition(property1.value, property2.value), animated)
    update()
    return CSRegistration(property1.onChange { update() }, property2.onChange { update() })
}

fun <T> View.visibleIf(
    property: CSHasChangeValue<T>, animated: Boolean = false,
    condition: (T) -> Boolean,
): CSRegistration {
    visible(condition(property.value))
    return property.onChange { visible(condition(property.value), animated) }
}

fun <T> View.invisibleIf(
    property: CSHasChangeValue<T>, animated: Boolean = false,
    condition: (T) -> Boolean,
): CSRegistration {
    invisible(condition(property.value))
    return property.onChange { invisible(condition(property.value), animated) }
}

fun View.invisibleIf(
    property: CSHasChangeValue<Boolean>, animated: Boolean = false,
): CSRegistration = invisibleIf(property, animated) { it }

fun View.isVisibleInParentRecursively(): Boolean {
    if (!isVisible) return false
    var view: View = this
    while (true) {
        val parent = view.parent
        when {
            parent == null -> return false
            parent !is View -> return true
            parent::class.java.name == "androidx.appcompat.widget.ContentFrameLayout" -> return true
            (parent.tag as? CSVisibility)?.isVisible?.isTrue == true -> return true
            !parent.isVisible -> return false
            else -> view = parent
        }
    }
}