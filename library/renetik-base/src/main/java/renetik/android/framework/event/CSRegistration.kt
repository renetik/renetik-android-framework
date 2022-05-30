package renetik.android.framework.event

import renetik.android.framework.Func

interface CSRegistration {
    var isActive: Boolean
    fun cancel() {
        isActive = false
    }

    companion object {
        fun pause(vararg registrations: CSRegistration, function: Func) {
            registrations.onEach { it.isActive = false }
            function()
            registrations.onEach { it.isActive = true }
        }

        fun registration(onCancel: Func? = null) = object : CSRegistration {
            override var isActive = true
            override fun cancel() {
                super.cancel()
                onCancel?.invoke()
            }
        }
    }
}