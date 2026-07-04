package renetik.android.core.android.content

import android.content.Context
import android.content.ContextWrapper.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.TRANSPORT_BLUETOOTH
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_ETHERNET
import android.net.NetworkCapabilities.TRANSPORT_VPN
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.Q

val Context.isNetworkConnected: Boolean
    get() {
        val manager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (SDK_INT >= Q)
            manager.getNetworkCapabilities(manager.activeNetwork)?.let {
                it.hasTransport(TRANSPORT_WIFI) ||
                        it.hasTransport(TRANSPORT_CELLULAR) ||
                        it.hasTransport(TRANSPORT_BLUETOOTH) ||
                        it.hasTransport(TRANSPORT_ETHERNET) ||
                        it.hasTransport(TRANSPORT_VPN)
            } ?: false
        else
            @Suppress("DEPRECATION")
            manager.activeNetworkInfo?.isConnected == true
    }
