package renetik.android.framework.task

import renetik.kotlin.later

class CSLaterOnce(val interval: Int = 0, val function: () -> Unit) {
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