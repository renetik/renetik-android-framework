package renetik.android.framework

import android.content.BroadcastReceiver
import android.content.Context
import android.content.ContextWrapper
import renetik.android.app.fixInputMethodLeak
import renetik.android.framework.CSApplication.Companion.application
import renetik.android.framework.common.catchAllWarn
import renetik.android.framework.event.*
import renetik.android.framework.event.CSEvent.Companion.event

abstract class CSContext : ContextWrapper, CSHasContext {
    constructor() : super(application)
    constructor(context: CSContext) : this(context as CSHasContext)
    constructor(context: CSHasContext) : super(context.context) {
        register(context.eventDestroy.listenOnce { onDestroy() })
    }

    constructor(context: Context) : super(context)

    private var _isDestroyed = false
    val isDestroyed get() = _isDestroyed
    override val eventDestroy = event<Unit>()
    override val context: Context get() = this
    final override val registrations = CSRegistrations()

    override fun unregisterReceiver(receiver: BroadcastReceiver) =
        catchAllWarn { super.unregisterReceiver(receiver) }

    override fun onDestroy() {
        registrations.cancel()
        // _isDestroyed should be set before event so we now we should not
        // destroy again in some actions as remove from superview
        _isDestroyed = true
        fixInputMethodLeak()
        eventDestroy.fire().clear()
    }
}