package renetik.android.viewbase

import android.app.ActivityManager
import android.app.Service
import android.content.*
import android.content.pm.PackageManager.GET_SIGNATURES
import android.content.pm.PackageManager.NameNotFoundException
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.content.res.Resources.NotFoundException
import android.net.ConnectivityManager
import android.net.Uri
import android.os.BatteryManager
import android.text.format.DateFormat.getDateFormat
import android.text.format.DateFormat.getTimeFormat
import android.util.Base64
import android.view.Display
import android.view.WindowManager
import androidx.core.content.ContextCompat
import renetik.android.application
import renetik.android.extensions.NO
import renetik.android.extensions.notNull
import renetik.android.java.collections.CSList
import renetik.android.java.collections.list
import renetik.android.lang.CSLang.set
import renetik.android.lang.tryAndError
import renetik.android.lang.tryAndFinally
import renetik.android.lang.tryAndWarn
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.security.MessageDigest
import java.util.*

abstract class CSContextController : ContextWrapper {

    constructor() : super(application) {}

    constructor(context: Context) : super(context) {}

    @Suppress("UNCHECKED_CAST")
    fun <Type : Any> service(serviceName: String) = getSystemService(serviceName) as Type

    @Suppress("DEPRECATION")
    val versionString
        get() = packageInfo!!.versionCode.toString() + "-" + packageInfo!!.versionName

    @Suppress("DEPRECATION")
    val appKeyHash
        get() = tryAndError {
            val info = packageManager.getPackageInfo(packageName, GET_SIGNATURES)
            if (set(info.signatures)) {
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
        get() = service<ConnectivityManager>(Context.CONNECTIVITY_SERVICE).activeNetworkInfo?.isConnected
                ?: NO

    protected val batteryPercent: Float
        get() {
            val batteryStatus = this.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            val level = batteryStatus!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            return level / scale.toFloat()
        }

    val defaultDisplay: Display get() = service<WindowManager>(Context.WINDOW_SERVICE).defaultDisplay

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

    fun stringRes(id: Int) = resources.getString(id)

    fun getResource(id: Int) = tryAndWarn {
        val stream = resources.openRawResource(id)
        val outputStream = ByteArrayOutputStream()
        val buffer = ByteArray(4 * 1024)
        tryAndFinally({
            var read: Int
            do {
                read = stream.read(buffer, 0, buffer.size)
                if (read == -1) break
                outputStream.write(buffer, 0, read)
            } while (true)
            outputStream.toByteArray()
        }) { stream.close() }
    }

    fun dimension(id: Int) = resources.getDimension(id).toInt()
    fun color(color: Int) = ContextCompat.getColor(this, color)
    fun timeFormatParse(text: String) = tryAndWarn { getTimeFormat(this).parse(text) }
    fun dateFormatParse(text: String) = tryAndWarn { getDateFormat(this).parse(text) }
    fun dateFormat(date: Date) = date.notNull { getDateFormat(this).format(date) }
    fun timeFormat(date: Date) = date.notNull { getTimeFormat(this).format(date) }

    fun getStringList(id: Int): CSList<String>? = tryAndWarn(NotFoundException::class) {
        list(*resources.getStringArray(id))
    }

    fun getIntList(id: Int) = tryAndWarn(NotFoundException::class) {
        list(resources.getIntArray(id).asList())
    }

    fun openInputStream(uri: Uri) = tryAndError(FileNotFoundException::class) {
        contentResolver.openInputStream(uri)
    }

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


