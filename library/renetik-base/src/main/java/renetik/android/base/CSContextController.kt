package renetik.android.base

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.Service
import android.content.*
import android.content.Context.*
import android.content.pm.PackageManager.GET_SIGNATURES
import android.content.pm.PackageManager.NameNotFoundException
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.net.ConnectivityManager
import android.os.BatteryManager
import android.text.format.DateFormat.getDateFormat
import android.text.format.DateFormat.getTimeFormat
import android.util.Base64
import android.view.Display
import android.view.WindowManager
import renetik.android.java.common.tryAndError
import renetik.android.java.common.tryAndWarn
import renetik.android.java.extensions.isSet
import renetik.android.java.extensions.notNull
import java.security.MessageDigest
import java.util.*

abstract class CSContextController : ContextWrapper {

    constructor() : super(application) {}

    constructor(context: Context) : super(context) {}

    val context: Context get() = this

    @Suppress("UNCHECKED_CAST")
    fun <Type : Any> service(serviceName: String) = getSystemService(serviceName) as Type

    @Suppress("DEPRECATION")
    val versionString
        get() = packageInfo!!.versionCode.toString() + "-" + packageInfo!!.versionName

    @Suppress("DEPRECATION")
    val appKeyHash
        get() = tryAndError {
            val info = packageManager.getPackageInfo(packageName, GET_SIGNATURES)
            if (info.signatures.isSet) {
                val messageDigest = MessageDigest.getInstance("SHA")
                messageDigest.update(info.signatures[0].toByteArray())
                Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT)
            } else null
        }

    val packageInfo
        get() = tryAndWarn(NameNotFoundException::class) {
            packageManager.getPackageInfo(packageName, 0)
        }

    val isNetworkConnected
        @SuppressLint("MissingPermission")
        get() = service<ConnectivityManager>(CONNECTIVITY_SERVICE)
                .activeNetworkInfo?.isConnected ?: false

    protected val batteryPercent: Float
        get() {
            val batteryStatus = this.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            val level = batteryStatus!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            return level / scale.toFloat()
        }

    val defaultDisplay: Display get() = service<WindowManager>(WINDOW_SERVICE).defaultDisplay

    @Suppress("DEPRECATION")
    val displayWidth
        get() = defaultDisplay.width

    @Suppress("DEPRECATION")
    val displayHeight
        get() = defaultDisplay.height

    val statusBarHeight: Int
        get() {
            val resource = resources.getIdentifier("status_bar_height", "dimen", "android")
            return if (resource > 0) resources.getDimensionPixelSize(resource) else 0
        }
    val isPortrait get() = resources.configuration.orientation == ORIENTATION_PORTRAIT
    val isLandscape get() = !isPortrait

    override fun unregisterReceiver(receiver: BroadcastReceiver) {
        tryAndWarn { super.unregisterReceiver(receiver) }
    }

    fun parseTimeFormat(text: String) = tryAndWarn { getTimeFormat(this).parse(text) }
    fun parseDateFormat(text: String) = tryAndWarn { getDateFormat(this).parse(text) }
    fun formatDate(date: Date) = date.notNull { getDateFormat(this).format(date) }
    fun formatTime(date: Date) = date.notNull { getTimeFormat(this).format(date) }

    fun isServiceRunning(serviceClass: Class<out Service>): Boolean {
        @Suppress("DEPRECATION")
        for (running in service<ActivityManager>(Context.ACTIVITY_SERVICE).getRunningServices(Integer.MAX_VALUE))
            if (serviceClass.name == running.service.className) return true
        return false
    }

    fun startService(serviceClass: Class<out Service>) = startService(Intent(this, serviceClass))
    fun stopService(serviceClass: Class<out Service>) = stopService(Intent(this, serviceClass))
    protected open fun onDestroy() = Unit
}




