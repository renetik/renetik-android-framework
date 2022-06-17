package renetik.android.framework.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.ContextWrapper
import renetik.android.app.fixInputMethodLeak
import renetik.android.core.lang.catchAllWarn
import renetik.android.core.CSApplication.Companion.app
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.registration.CSRegistrations
import renetik.android.event.fire
import renetik.android.event.listenOnce
import renetik.android.event.register
import renetik.android.framework.protocol.CSHasContext

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