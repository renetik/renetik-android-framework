package renetik.android.view.extensions

import android.view.View
import android.view.View.*
import androidx.appcompat.widget.ContentFrameLayout
import renetik.android.framework.event.CSEvent.CSEventRegistration
import renetik.android.framework.event.CSVisibility
import renetik.android.framework.event.CSVisibleEventOwner
import renetik.android.framework.event.property.CSEventProperty

fun <T : View> T.visible() = apply {
    visibility = VISIBLE
    (tag as? CSVisibility)?.updateVisibility()
}

fun <T : View> T.invisible() = apply {
    visibility = INVISIBLE
    (tag as? CSVisibility)?.updateVisibility()
}

fun <T : View> T.gone() = apply {
    visibility = GONE
    (tag as? CSVisibility)?.updateVisibility()
}

val <T : View> T.isVisible get() = visibility == VISIBLE
val <T : View> T.isInvisible get() = visibility == INVISIBLE
val <T : View> T.isGone get() = visibility == GONE

fun <T : View> T.show(animated: Boolean = false) = apply { if (animated) fadeIn() else visible() }
fun <T : View> T.hide(animated: Boolean = false) = apply { if (animated) fadeOut() else gone() }

fun <T : View> T.visibleIf(condition: Boolean) = apply { if (condition) visible() else invisible() }
fun <T : View> T.invisibleIf(condition: Boolean) =
    apply { if (condition) invisible() else visible() }

fun <T : View> T.shownIf(condition: Boolean, animated: Boolean = false) = apply {
    if (condition) show(animated) else hide(animated)
}

fun <T : View> T.goneIf(condition: Boolean, animated: Boolean = false) =
    shownIf(!condition, animated)

fun View.shownIfSet(parent: CSVisibleEventOwner, property: CSEventProperty<Any?>) =
    apply {
        fun updateVisibility() = shownIf(property.value != null)
        parent.whileVisible(property.onChange { updateVisibility() })
        updateVisibility()
    }

fun View.shownIf(property: CSEventProperty<Boolean>,
                 animated: Boolean = false): CSEventRegistration {
    fun updateVisibility() = shownIf(property.value, animated)
    updateVisibility()
    return property.onChange { updateVisibility() }
}

fun View.shownIfFalse(property: CSEventProperty<Boolean>,
                      animated: Boolean = false) = goneIfTrue(property, animated)

fun View.goneIfTrue(property: CSEventProperty<Boolean>,
                    animated: Boolean = false): CSEventRegistration {
    fun updateVisibility() = goneIf(property.value, animated)
    updateVisibility()
    return property.onChange { updateVisibility() }
}

fun <T> View.shownIfEquals(parent: CSVisibleEventOwner,
                           property: CSEventProperty<T?>,
                           value: T) = apply {
    fun updateVisibility() = shownIf(property.value == value)
    parent.whileVisible(property.onChange { updateVisibility() })
    updateVisibility()
}

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