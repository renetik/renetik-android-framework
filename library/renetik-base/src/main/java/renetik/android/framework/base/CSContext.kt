package renetik.android.framework.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.ContextWrapper
import renetik.android.app.fixInputMethodLeak
import renetik.android.framework.base.CSApplication.Companion.app
import renetik.android.framework.event.*
import renetik.android.framework.event.CSEvent.Companion.event
import renetik.android.framework.lang.catchAllWarn

abstract class CSContext : ContextWrapper, CSHasContext {
    constructor() : super(app)
    constructor(context: Context) : super(context)
    constructor(context: CSContext) : this(context as CSHasContext)
    constructor(context: CSHasContext) : super(context.context) {
        register(context.eventDestroy.listenOnce { onDestroy() })
    }

    final override val registrations = CSRegistrations()
    override val eventDestroy = event<Unit>()
    var isDestroyed = false
        private set


    override fun onDestroy() {
        registrations.cancel()
        isDestroyed = true
        eventDestroy.fire().clear()
        fixInputMethodLeak()
    }

    override val context: Context get() = this

    override fun unregisterReceiver(receiver: BroadcastReceiver) =
        catchAllWarn { super.unregisterReceiver(receiver) }
}