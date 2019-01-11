package renetik.android.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View

@Suppress("UNCHECKED_CAST")
fun <ViewType : View> Context.inflate(layoutId: Int) =
        LayoutInflater.from(this).inflate(layoutId, null) as ViewType

val Context.applicationLabel: String get() = "${applicationInfo.loadLabel(packageManager)}"

val Context.applicationLogo: Drawable? get() = applicationInfo.loadLogo(packageManager)

/**
 * If the item does not have an icon, the item's default icon is returned
 * such as the default activity icon.
 */
val Context.applicationIcon: Drawable get() = applicationInfo.loadIcon(packageManager)