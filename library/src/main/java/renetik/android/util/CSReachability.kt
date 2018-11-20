package renetik.android.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager.CONNECTIVITY_ACTION
import renetik.android.extensions.asString
import renetik.java.event.event
import renetik.android.view.base.CSContextController
import renetik.java.lang.CSLang

class CSReachability : CSContextController() {

    private val eventOnConnected = event<CSReachability>()
    private val eventOnDisConnected = event<CSReachability>()
    private val eventOnStateChanged = event<CSReachability>()

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            intent.asString()
            onNetworkStateChange()
        }
    }
    var started: Boolean = false

    fun start(): CSReachability {
        if (!started) {
            @Suppress("DEPRECATION")
            registerReceiver(receiver, IntentFilter(CONNECTIVITY_ACTION))
            started = CSLang.YES
        }
        return this
    }

    fun stop(): CSReachability {
        if (started) {
            started = CSLang.NO
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
