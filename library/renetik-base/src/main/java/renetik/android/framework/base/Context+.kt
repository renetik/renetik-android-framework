package renetik.android.framework.base

import android.content.Context
import renetik.android.framework.base.CSApplication.Companion.app

val Context.isDevelopment get() = app.isDevelopmentMode
val Context.isDebug get() = app.isDebugBuild