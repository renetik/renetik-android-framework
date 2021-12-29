package renetik.android.framework

import android.app.ActivityManager
import android.app.Service
import android.content.*
import android.os.BatteryManager
import android.text.format.DateFormat.getDateFormat
import android.text.format.DateFormat.getTimeFormat
import android.util.DisplayMetrics
import android.view.WindowManager
import renetik.android.framework.CSApplication.Companion.application
import renetik.android.framework.common.catchAllWarn
import renetik.android.framework.event.*
import renetik.kotlin.notNull
import java.util.*

private val LOW_DPI_STATUS_BAR_HEIGHT = 19
private val MEDIUM_DPI_STATUS_BAR_HEIGHT = 25
private val HIGH_DPI_STATUS_BAR_HEIGHT = 38

abstract class CSContext : ContextWrapper, CSHasContext {
    constructor() : super(application)
    constructor(context: CSContext) : this(context as CSHasContext)
    constructor(context: CSHasContext) : super(context.context) {
        eventRegistrations.add(context.eventDestroy.listenOnce { onDestroy() })
    }

    constructor(context: Context) : super(context)

    private var _isDestroyed = false
    val isDestroyed get() = _isDestroyed
    override val eventDestroy = event<Unit>()

    override val context: Context get() = this

    protected val batteryPercent: Float
        get() {
            val batteryStatus =
                this.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            val level = batteryStatus!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            return level / scale.toFloat()
        }

    private val defaultDisplay get() = (getSystemService(WINDOW_SERVICE) as WindowManager).defaultDisplay

    @Suppress("DEPRECATION")
    val displayWidth: Int
        get() = defaultDisplay.width

    @Suppress("DEPRECATION")
    val displayHeight
        get() = defaultDisplay.height

    private val displayMetrics get() = DisplayMetrics().apply { defaultDisplay.getMetrics(this) }

    val realDisplayMetrics get() = DisplayMetrics().apply { defaultDisplay.getRealMetrics(this) }

    open val statusBarHeight
        get() = when (displayMetrics.densityDpi) {
            DisplayMetrics.DENSITY_HIGH -> HIGH_DPI_STATUS_BAR_HEIGHT
            DisplayMetrics.DENSITY_MEDIUM -> MEDIUM_DPI_STATUS_BAR_HEIGHT
            DisplayMetrics.DENSITY_LOW -> LOW_DPI_STATUS_BAR_HEIGHT
            else -> MEDIUM_DPI_STATUS_BAR_HEIGHT
        }

    override fun unregisterReceiver(receiver: BroadcastReceiver) =
        catchAllWarn { super.unregisterReceiver(receiver) }

    fun parseTimeFormat(text: String) = catchAllWarn { getTimeFormat(this).parse(text) }
    fun parseDateFormat(text: String) = catchAllWarn { getDateFormat(this).parse(text) }
    fun formatDate(date: Date) = date.notNull { getDateFormat(this).format(date) }
    fun formatTime(date: Date) = date.notNull { getTimeFormat(this).format(date) }

    fun isServiceRunning(serviceClass: Class<out Service>): Boolean {
        @Suppress("DEPRECATION")
        for (running in (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).getRunningServices(
            Integer.MAX_VALUE))
            if (serviceClass.name == running.service.className) return true
        return false
    }

    fun startService(serviceClass: Class<out Service>): ComponentName? =
        startService(Intent(this, serviceClass))

    fun stopService(serviceClass: Class<out Service>) = stopService(Intent(this, serviceClass))

    override fun onDestroy() {
        eventRegistrations.cancel()
        // _isDestroyed should be set before event so we now we should not
        // destroy again in some actions as remove from superview
        _isDestroyed = true
        eventDestroy.fire().clear()
    }

    final override val eventRegistrations = CSEventRegistrations()
}

fun CSContext.register(intent: IntentFilter, receiver: (Intent, BroadcastReceiver) -> void) =
    registerReceiver(object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) = receiver(intent, this)
    }, intent)

fun CSContext.unregister(receiver: BroadcastReceiver) {
    unregisterReceiver(receiver)
}




