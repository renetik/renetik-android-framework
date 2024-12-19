@file:Suppress("DEPRECATION")

package renetik.android.ui.extensions

import android.app.Activity
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.content.res.Configuration.ORIENTATION_SQUARE
import android.view.ViewGroup
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

//fun Context.fixInputMethodLeak() {
//    for (declaredField in input::class.java.declaredFields) try {
//        if (!declaredField.isAccessible) declaredField.isAccessible = true
//        val obj = declaredField.get(input)
//        if (obj == null || obj !is View) continue
//        if (obj.context === this) declaredField.set(input, null) else continue
//    } catch (throwable: Throwable) {
//        logDebug { message(throwable) }
//    }
//}
//
//fun Context.fixInputMethod() {
//    var inputMethodManager: InputMethodManager? = null
//    try {
//        inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//    } catch (th: Throwable) {
//        th.printStackTrace()
//    }
//    if (inputMethodManager == null) {
//        return
//    }
//    for (declaredField in inputMethodManager.javaClass.declaredFields) {
//        try {
//            if (!declaredField.isAccessible) declaredField.isAccessible = true
//            val obj: Any? = declaredField.get(inputMethodManager)
//            if (obj !is View) continue
//            if (obj.context === this)
//                declaredField.set(inputMethodManager, null)
//            else continue
//        } catch (th: Throwable) {
//            th.printStackTrace()
//        }
//    }
//}
//
//fun fixInputMethodManagerLeak(destContext: Context?) {
//    if (destContext == null) {
//        return
//    }
//    val imm = destContext.getSystemService(INPUT_METHOD_SERVICE)
//            as? InputMethodManager ?: return
//    val arr = arrayOf("mCurRootView", "mServedView", "mNextServedView")
//    var f: Field? = null
//    var obj_get: Any? = null
//    for (i in arr.indices) {
//        val param = arr[i]
//        try {
//            f = imm.javaClass.getDeclaredField(param)
//            if (!f.isAccessible) {
//                f.isAccessible = true
//            }
//            obj_get = f.get(imm)
//            if (obj_get != null && obj_get is View) {
//                val v_get = obj_get as View?
//                if (v_get!!.context === destContext) { //The context referenced by InputMethodManager is the one that wants the target to be destroyed
//                    f.set(imm, null) //Make it empty and destroy the path to gc node
//                } else {
//                    println("fixInputMethodManagerLeak break, context is not suitable," +
//                            " get_context =" + v_get!!.context + "dest_context=" + destContext)
//                    break
//                }
//            }
//        } catch (t: Throwable) {
//            t.printStackTrace()
//        }
//    }
//}
//
//fun fixInputMethodManagerLeak2(context: Context) {
//    try {
//        val manager = context.getSystemService(INPUT_METHOD_SERVICE)
//                as? InputMethodManager ?: return
//        val fieldNames = arrayOf("mCurRootView", "mServedView", "mNextServedView")
//        for (index in fieldNames.indices) {
//            try {
//                val field = manager.javaClass.getDeclaredField(fieldNames[index])
//                if (!field.isAccessible) field.isAccessible = true
//                val value = field[manager]
//                if (value != null) field[manager] = null
//            } catch (t: Throwable) {
//            }
//        }
//    } catch (t: Throwable) {
//        t.printStackTrace()
//    }
//}