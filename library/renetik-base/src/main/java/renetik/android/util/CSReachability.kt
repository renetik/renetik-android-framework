package renetik.android.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager.CONNECTIVITY_ACTION
import renetik.android.base.CSContextController
import renetik.android.extensions.asString
import renetik.android.extensions.isNetworkConnected
import renetik.android.java.event.event
import renetik.android.logging.CSLog.logInfo

class CSReachability : CSContextController() {

    val eventOnConnected = event<CSReachability>()
    val eventOnDisConnected = event<CSReachability>()
    val eventOnStateChanged = event<CSReachability>()

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            logInfo(intent.asString)
            onNetworkStateChange()
        }
    }
    var started: Boolean = false

    fun start(): CSReachability {
        if (!started) {
            @Suppress("DEPRECATION")
            registerReceiver(receiver, IntentFilter(CONNECTIVITY_ACTION))
            started = true
        }
        return this
    }

    fun stop(): CSReachability {
        if (started) {
            started = false
            unregisterReceiver(receiver)
        }
        return this
    }

    private fun onNetworkStateChange() {
        if (isNetworkConnected) onNetworkConnected()
        else onNetworkDisconnected()
        eventOnStateChanged.fire(this)
    }

    protected fun onNetworkConnected() {
        eventOnConnected.fire(this)
    }

    protected fun onNetworkDisconnected() {
        eventOnDisConnected.fire(this)
    }
}
