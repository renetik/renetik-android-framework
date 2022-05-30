package renetik.android.framework.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager.CONNECTIVITY_ACTION
import renetik.android.content.asString
import renetik.android.content.isNetworkConnected
import renetik.android.framework.base.CSContext
import renetik.android.framework.event.CSEvent.Companion.event
import renetik.android.framework.logging.CSLog.debug

class CSReachability : CSContext() {

    val eventOnConnected = event<CSReachability>()
    val eventOnDisConnected = event<CSReachability>()
    val eventOnStateChanged = event<CSReachability>()

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            debug { intent.asString }
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
