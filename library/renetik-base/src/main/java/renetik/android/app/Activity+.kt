package renetik.android.app

import android.app.Activity
import android.content.res.Configuration.*
import android.view.View
import renetik.android.content.isLandscape

val Activity.contentView1
    get() = window.findViewById<View>(android.R.id.content)

val Activity.contentView2
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