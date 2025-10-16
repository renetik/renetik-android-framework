@file:Suppress("DEPRECATION")

package renetik.android.ui.extensions

import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.ContextWrapper
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.content.res.Configuration.ORIENTATION_SQUARE
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.TIRAMISU
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import renetik.android.core.logging.CSLog.logDebug
import renetik.android.ui.extensions.view.view
import java.lang.reflect.Field

fun Activity.disablePendingAnimations() {
    if (SDK_INT >= TIRAMISU) overridePendingTransition(0, 0, 0)
    else overridePendingTransition(0, 0)
}

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

fun Activity.fixInputMethodManagerLeak() {
    fun findFieldInHierarchy(clazz: Class<*>, name: String): Field? {
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
    runCatching {
        val manager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager ?: return
        runCatching {
            currentFocus?.clearFocus()
            currentFocus?.windowToken?.let { manager.hideSoftInputFromWindow(it, 0) }
            window?.decorView?.rootView?.clearFocus()
        }.onFailure {
            Log.w("LeakFix", "Public IMM cleanup failed", it)
        }
        val fieldNames = arrayOf("mCurRootView", "mServedView", "mNextServedView")
        for (name in fieldNames) runCatching inner@{
            val field = findFieldInHierarchy(manager.javaClass, name)
                ?.apply { isAccessible = true }
            val value = field?.get(manager) ?: return@inner

            // handle direct View
            if (value is View) {
                val viewContext = value.context
                if (isContextMatchingActivity(viewContext)) field.set(manager, null)
                return@inner
            }

            // handle WeakReference<View> or other refs
            val referencedView = when (value) {
                is java.lang.ref.WeakReference<*> -> value.get()
                is kotlin.jvm.internal.Ref.ObjectRef<*> ->
                    (value as? kotlin.jvm.internal.Ref.ObjectRef<*>)?.element
                else -> null
            }
            if (referencedView is View) {
                if (isContextMatchingActivity(referencedView.context)) field.set(manager, null)
            }
        }.onFailure { logDebug(it) { "LeakFix Failed clearing IMM field $name" } }
    }.onFailure { logDebug(it) { "LeakFix IMM leak fix failed" } }
}