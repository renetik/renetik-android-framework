package renetik.android.app

import android.app.Activity
import android.content.Context
import android.content.res.Configuration.*
import android.view.View
import renetik.android.core.extensions.content.input
import renetik.android.core.logging.CSLog.logDebug

val Activity.contentView1: View
    get() = window.findViewById(android.R.id.content)

val Activity.contentView2: View
    get() = window.decorView.rootView

val Activity.isScreenLandscape get() = screenOrientation == ORIENTATION_LANDSCAPE
val Activity.screenOrientation: Int
    get() {
        val display = getWindowManager().getDefaultDisplay()
        return when {
            display.getWidth() == display.getHeight() -> ORIENTATION_SQUARE
            display.getWidth() < display.getHeight() -> ORIENTATION_PORTRAIT
            else -> ORIENTATION_LANDSCAPE
        }
    }

@Deprecated("Does this work ?")
fun Context.fixInputMethodLeak() {
    for (declaredField in input::class.java.declaredFields) try {
        if (!declaredField.isAccessible) declaredField.isAccessible = true
        val obj = declaredField.get(input)
        if (obj == null || obj !is View) continue
        if (obj.context === this) declaredField.set(input, null) else continue
    } catch (throwable: Throwable) {
        logDebug(throwable)
    }
}