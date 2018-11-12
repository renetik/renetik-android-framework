package renetik.android.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager.CONNECTIVITY_ACTION
import renetik.android.extensions.NO
import renetik.android.extensions.YES
import renetik.android.java.event.event
import renetik.android.java.event.fire
import renetik.android.lang.CSLang.asString
import renetik.android.viewbase.CSContextController

class CSReachability : CSContextController() {

    private val eventOnConnected = event<CSReachability>()
    private val eventOnDisConnected = event<CSReachability>()
    private val eventOnStateChanged = event<CSReachability>()

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            asString(intent)
            onNetworkStateChange()
        }
    }
    var started: Boolean = false

    fun start(): CSReachability {
        if (!started) {
            context().registerReceiver(receiver, IntentFilter(CONNECTIVITY_ACTION))
            started = YES
        }
        return this
    }

    fun stop(): CSReachability {
        if (started) {
            started = NO
            unregisterReceiver(receiver)
        }
        return this
    }

    private fun onNetworkStateChange() {
        if (isNetworkConnected) onNetworkConnected()
        else onNetworkDisconnected()
        fire(eventOnStateChanged, this)
    }

    protected fun onNetworkConnected() {
        fire(eventOnConnected, this)
    }

    protected fun onNetworkDisconnected() {
        fire(eventOnDisConnected, this)
    }
}
