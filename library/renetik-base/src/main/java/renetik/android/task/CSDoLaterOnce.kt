package renetik.android.task

import renetik.android.java.extensions.later

class CSDoLaterOnce(val interval: Int = 0, val function: () -> Unit) {
    private var willInvoke = false

    fun start() {
        if (willInvoke) return
        willInvoke = true
        later(interval) {
            function()
            willInvoke = false
        }
    }
}