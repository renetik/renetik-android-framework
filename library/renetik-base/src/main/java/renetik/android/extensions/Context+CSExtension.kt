package renetik.android.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import renetik.android.java.common.tryAndError
import renetik.android.java.common.tryAndWarn
import renetik.android.java.extensions.isSet
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
val Context.appKeyHash
        get() = tryAndError {
                val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
                if (info.signatures.isSet) {
                        val messageDigest = MessageDigest.getInstance("SHA")
                        messageDigest.update(info.signatures[0].toByteArray())
                        Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT)
                } else null
        }

val Context.packageInfo
        get() = tryAndWarn(PackageManager.NameNotFoundException::class) {
                packageManager.getPackageInfo(packageName, 0)
        }



