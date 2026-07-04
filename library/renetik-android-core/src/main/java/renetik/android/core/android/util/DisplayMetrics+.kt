package renetik.android.core.android.util

import android.util.DisplayMetrics
import renetik.android.core.android.content.isLandscape
import renetik.android.core.base.CSApplication.Companion.app

val DisplayMetrics.orientedWidthPixels: Int
    get() = if (app.isLandscape) heightPixels else widthPixels

val DisplayMetrics.orientedHeightPixels: Int
    get() = if (app.isLandscape) widthPixels else heightPixels