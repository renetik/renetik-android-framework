package renetik.android.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.util.Base64
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.res.getDrawableOrThrow
import renetik.android.content.resourceColor
import renetik.android.framework.common.catchAllErrorReturnNull
import renetik.android.framework.common.catchWarnReturnNull
import renetik.android.primitives.isSet
import java.security.MessageDigest

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

@Suppress("UNCHECKED_CAST")
fun <Type : Any> Context.service(serviceName: String) = getSystemService(serviceName) as Type

val Context.isNetworkConnected
    @SuppressLint("MissingPermission")
    get() = service<ConnectivityManager>(ContextWrapper.CONNECTIVITY_SERVICE)
        .activeNetworkInfo?.isConnected ?: false

@Suppress("DEPRECATION")
val Context.versionString
    get() = packageInfo!!.versionCode.toString() + "-" + packageInfo!!.versionName

@Suppress("DEPRECATION")
val Context.versionCode
    get() = packageInfo!!.versionCode.toString()

@Suppress("DEPRECATION")
val Context.appKeyHash
    @SuppressLint("PackageManagerGetSignatures")
    get() = catchAllErrorReturnNull {
        val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
        if (info.signatures.isSet) {
            val messageDigest = MessageDigest.getInstance("SHA")
            messageDigest.update(info.signatures[0].toByteArray())
            Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT)
        } else null
    }

val Context.packageInfo
    get() = catchWarnReturnNull<PackageInfo, NameNotFoundException> {
        packageManager.getPackageInfo(packageName, 0)
    }

@SuppressLint("UseCompatLoadingForDrawables")
fun Context.getDrawable(name: String): Drawable? {
    val resourceId = resources.getIdentifier(name, "drawable", packageName)
    return resources.getDrawable(resourceId);
}

fun Context.getColorResource(name: String): Int? {
    val colorResource = resources.getIdentifier(name, "color", packageName)
    return if (colorResource == 0) null else colorResource
}

fun Context.getColor(name: String): Int? = getColorResource(name)?.let { resourceColor(it) }


val Context.progressDrawable: Drawable
    get() {
        val value = TypedValue()
        theme.resolveAttribute(android.R.attr.progressBarStyleSmall, value, false)
        val progressBarStyle = value.data
        val attributes = intArrayOf(android.R.attr.indeterminateDrawable)
        val array = obtainStyledAttributes(progressBarStyle, attributes)
        val drawable = array.getDrawableOrThrow(0)
        array.recycle()
        (drawable as? Animatable)?.start()
        return drawable
    }




