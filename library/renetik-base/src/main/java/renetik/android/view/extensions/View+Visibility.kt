package renetik.android.view.extensions

import android.view.View
import android.view.View.*
import androidx.appcompat.widget.ContentFrameLayout
import renetik.android.framework.event.CSVisibleEventOwner
import renetik.android.framework.event.property.CSEventProperty
import renetik.android.primitives.isTrue

val <T : View> T.isVisible get() = visibility == VISIBLE

val <T : View> T.isInvisible get() = visibility == INVISIBLE

val <T : View> T.isGone get() = visibility == GONE

fun <T : View> T.show() = apply { visibility = VISIBLE }

fun <T : View> T.visible() = apply { visibility = VISIBLE }

fun <T : View> T.hide() = apply { visibility = GONE }

fun <T : View> T.gone() = apply { visibility = GONE }

fun <T : View> T.invisible() = apply { visibility = INVISIBLE }

fun <T : View> T.visibleIf(condition: Boolean) = apply { if (condition) visible() else invisible() }

fun <T : View> T.invisibleIf(condition: Boolean) =
    apply { if (condition) invisible() else visible() }

fun <T : View> T.shownIf(condition: Boolean?, fade: Boolean = false) = apply {
    when {
        fade -> if (condition.isTrue) fadeIn() else fadeOut()
        condition.isTrue -> show()
        else -> gone()
    }
}

fun <T : View> T.hiddenIf(condition: Boolean?) = apply { if (condition.isTrue) gone() else show() }

fun View.visibilityPropertySet(parent: CSVisibleEventOwner, property: CSEventProperty<Any?>) =
    apply {
        fun updateVisibility() = shownIf(property.value != null)
        parent.whileShowing(property.onChange { updateVisibility() })
        updateVisibility()
    }

fun View.visibilityPropertyTrue(parent: CSVisibleEventOwner,
                                property: CSEventProperty<Boolean>,
                                fade: Boolean = true) =
    apply {
        fun updateVisibility() = shownIf(property.value, fade)
        parent.whileShowing(property.onChange { updateVisibility() })
        updateVisibility()
    }

fun <T> View.visibilityPropertyEquals(parent: CSVisibleEventOwner,
                                      property: CSEventProperty<T?>,
                                      value: T) = apply {
    fun updateVisibility() = shownIf(property.value == value)
    parent.whileShowing(property.onChange { updateVisibility() })
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