package renetik.android.content

import android.annotation.SuppressLint
import android.content.*
import android.content.ContextWrapper.WINDOW_SERVICE
import android.content.Intent.ACTION_BATTERY_CHANGED
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.os.BatteryManager
import android.util.Base64
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.annotation.StringRes
import androidx.core.content.res.getDrawableOrThrow
import renetik.android.framework.lang.catchAllErrorReturnNull
import renetik.android.framework.lang.catchWarnReturnNull
import renetik.android.framework.void
import renetik.android.primitives.isSet
import java.security.MessageDigest
import java.util.*

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

val Context.isNetworkConnected
    @SuppressLint("MissingPermission")
    get() = (getSystemService(ContextWrapper.CONNECTIVITY_SERVICE) as ConnectivityManager)
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
    return resources.getDrawable(resourceId)
}

fun Context.getColorResource(name: String): Int? {
    val colorResource = resources.getIdentifier(name, "color", packageName)
    return if (colorResource == 0) null else colorResource
}

fun Context.getColor(name: String): CSColorInt? = getColorResource(name)?.let { color(it) }

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

fun Context.register(intent: IntentFilter, receiver: (Intent, BroadcastReceiver) -> void) =
    registerReceiver(object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) = receiver(intent, this)
    }, intent)

fun Context.unregister(receiver: BroadcastReceiver) {
    unregisterReceiver(receiver)
}

val Context.batteryPercent: Float
    get() {
        val batteryStatus = registerReceiver(null, IntentFilter(ACTION_BATTERY_CHANGED))
        val level = batteryStatus!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        return level / scale.toFloat()
    }

@Suppress("DEPRECATION")
val Context.defaultDisplay: Display
    get() = (getSystemService(WINDOW_SERVICE) as WindowManager).defaultDisplay

@Suppress("DEPRECATION")
val Context.displayWidth: Int
    get() = defaultDisplay.width

@Suppress("DEPRECATION")
val Context.displayHeight
    get() = defaultDisplay.height

private val Context.displayMetrics2
    get() = DisplayMetrics().apply {
        defaultDisplay.getMetrics(this)
    }

val Context.realDisplayMetrics
    get() = DisplayMetrics().apply {
        defaultDisplay.getRealMetrics(this)
    }

fun Context.string(@StringRes resId: Int): String {
    return resources.getString(resId)
}

fun Context.setLocale(locale: Locale) {
    Locale.setDefault(locale)
    val config = resources.configuration
    config.setLocale(locale)
    config.setLayoutDirection(locale)
    @Suppress("DEPRECATION")
    resources.updateConfiguration(config, resources.displayMetrics)
}

fun Context.createContextForLocale(locale: Locale): Context {
    Locale.setDefault(locale)
    val config = resources.configuration
    config.setLocale(locale)
    config.setLayoutDirection(locale)
    return createConfigurationContext(config)
}