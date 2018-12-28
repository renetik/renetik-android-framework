package renetik.android.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import renetik.android.java.extensions.stringify

@Suppress("UNCHECKED_CAST")
fun <ViewType : View> Context.inflate(layoutId: Int) =
        LayoutInflater.from(this).inflate(layoutId, null) as ViewType

fun Context.applicationLabel(): String = applicationInfo.loadLabel(packageManager).stringify()

fun Context.applicationLogo(): Drawable? = applicationInfo.loadLogo(packageManager)

fun Context.applicationIcon(): Drawable? = applicationInfo.loadIcon(packageManager)