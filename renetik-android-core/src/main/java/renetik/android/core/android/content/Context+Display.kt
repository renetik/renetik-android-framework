package renetik.android.core.android.content

import android.content.Context
import android.content.ContextWrapper.WINDOW_SERVICE
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.content.res.Configuration.ORIENTATION_UNDEFINED
import android.util.DisplayMetrics
import android.view.Display
import android.view.Surface.ROTATION_0
import android.view.Surface.ROTATION_180
import android.view.Surface.ROTATION_270
import android.view.Surface.ROTATION_90
import android.view.WindowManager
import renetik.android.core.android.content.CSDisplayOrientation.LandscapeLeft
import renetik.android.core.android.content.CSDisplayOrientation.LandscapeRight
import renetik.android.core.android.content.CSDisplayOrientation.Portrait
import renetik.android.core.android.content.CSDisplayOrientation.Unknown
import renetik.android.core.android.content.CSDisplayOrientation.UpsideDown
import renetik.android.core.kotlin.equalsAny

@Suppress("DEPRECATION")
val Context.defaultDisplay: Display
    get() = (getSystemService(WINDOW_SERVICE) as WindowManager).defaultDisplay

@Suppress("DEPRECATION")
val Context.displayWidth: Int
    get() = defaultDisplay.width

@Suppress("DEPRECATION")
val Context.displayHeight
    get() = defaultDisplay.height

@Suppress("DEPRECATION")
private val Context.displayMetrics2
    get() = DisplayMetrics().apply {
        defaultDisplay.getMetrics(this)
    }

@Suppress("DEPRECATION")
val Context.realDisplayMetrics
    get() = DisplayMetrics().apply {
        defaultDisplay.getRealMetrics(this)
    }

val CSDisplayOrientation.isLandscape: Boolean
    get() = equalsAny(LandscapeLeft, LandscapeRight)

val CSDisplayOrientation.isPortrait: Boolean
    get() = equalsAny(Portrait, UpsideDown)

val Context.deviceDefaultOrientation: Int
    get() {
        val config = resources.configuration
        val rotation = defaultDisplay.rotation
        return if (rotation == ORIENTATION_UNDEFINED) ORIENTATION_UNDEFINED
        else if ((rotation == ROTATION_0 ||
                    rotation == ROTATION_180) && config.orientation == ORIENTATION_LANDSCAPE
            || (rotation == ROTATION_90 ||
                    rotation == ROTATION_270) && config.orientation == ORIENTATION_PORTRAIT)
            ORIENTATION_LANDSCAPE
        else ORIENTATION_PORTRAIT
    }

val Context.orientation
    get() = if (deviceDefaultOrientation == ORIENTATION_PORTRAIT)
        orientationPhone else orientationTablet

private val Context.orientationPhone
    get() = when (defaultDisplay.rotation) {
        ROTATION_0 -> Portrait
        ROTATION_90 -> LandscapeRight
        ROTATION_180 -> UpsideDown
        ROTATION_270 -> LandscapeLeft
        else -> Unknown
    }

private val Context.orientationTablet
    get() = when (defaultDisplay.rotation) {
        ROTATION_0 -> LandscapeRight
        ROTATION_90 -> UpsideDown
        ROTATION_180 -> LandscapeLeft
        ROTATION_270 -> Portrait
        else -> Unknown
    }