@file:Suppress("DEPRECATION")

package renetik.android.ui.extensions

import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.ContextWrapper
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.content.res.Configuration.ORIENTATION_SQUARE
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import renetik.android.ui.extensions.view.view

fun Activity.clearContentView() {
    window.decorView.view<ViewGroup>(android.R.id.content).removeAllViews()
}

val Activity.isWindowLandscape get() = windowOrientation == ORIENTATION_LANDSCAPE

val Activity.windowOrientation: Int
    get() {
        val display = windowManager.defaultDisplay
        return when {
            display.width == display.height -> ORIENTATION_SQUARE
            display.width < display.height -> ORIENTATION_PORTRAIT
            else -> ORIENTATION_LANDSCAPE
        }
    }

fun Activity.fixInputMethodManagerLeakSafe() {
    fun findFieldInHierarchy(clazz: Class<*>, name: String): java.lang.reflect.Field? {
        var current: Class<*>? = clazz
        while (current != null) try {
            return current.getDeclaredField(name)
        } catch (_: NoSuchFieldException) {
            current = current.superclass
        }
        return null
    }

    fun Activity.isContextMatchingActivity(ctx: Context?): Boolean {
        if (ctx == null) return false
        if (ctx === this) return true
        if (ctx is ContextWrapper && ctx.baseContext === this) return true
        return false
    }
    try {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager ?: return
        val fieldNames = arrayOf("mCurRootView", "mServedView", "mNextServedView")
        for (name in fieldNames) {
            try {
                val field = findFieldInHierarchy(imm.javaClass, name)
                    ?.apply { isAccessible = true }
                val value = field?.get(imm) ?: continue

                // handle direct View
                if (value is View) {
                    val viewContext = value.context
                    if (isContextMatchingActivity(viewContext)) {
                        field.set(imm, null)
                    }
                    continue
                }

                // handle WeakReference<View> or other refs
                val referencedView = when (value) {
                    is java.lang.ref.WeakReference<*> -> value.get()
                    is kotlin.jvm.internal.Ref.ObjectRef<*> ->
                        (value as? kotlin.jvm.internal.Ref.ObjectRef<*>)?.element
                    else -> null
                }
                if (referencedView is View) {
                    if (isContextMatchingActivity(referencedView.context)) {
                        field.set(imm, null)
                    }
                }
            } catch (e: Throwable) {
                Log.w("LeakFix", "Failed clearing IMM field $name", e)
            }
        }
    } catch (e: Throwable) {
        Log.w("LeakFix", "IMM leak fix failed", e)
    }
}