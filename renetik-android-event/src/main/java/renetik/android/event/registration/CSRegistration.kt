package renetik.android.event.registration

import androidx.annotation.AnyThread
import renetik.android.core.lang.ArgFun
import renetik.android.core.lang.CSHasCancel
import renetik.android.core.lang.Fun
import renetik.android.event.CSEvent
import renetik.android.event.lifecycle.CSHasDestruct
import java.io.Closeable

interface CSRegistration : CSHasCancel, Closeable, CSHasDestruct {
    val isActive: Boolean
    val isCanceled: Boolean
    val eventCancel: CSEvent<Unit>
    override val isDestructed: Boolean get() = isCanceled
    override val eventDestruct: CSEvent<Unit> get() = eventCancel
    override fun onDestruct() = Unit

    @AnyThread
    fun resume()

    @AnyThread
    fun pause()

    @AnyThread
    override fun cancel()

    companion object {
        val Empty = object : CSRegistration {
            override val isActive: Boolean = false
            override val isCanceled: Boolean = false
            override val eventCancel: CSEvent<Unit> = CSEvent.Empty
            override fun resume() = Unit
            override fun pause() = Unit
            override fun cancel() = Unit
        }

        fun paused(registration: CSRegistration?, function: Fun) {
            registration?.pause()
            function()
            registration?.resume()
        }

        fun paused(vararg registrations: CSRegistration, function: Fun) {
            registrations.onEach { it.pause() }
            function()
            registrations.onEach { it.resume() }
        }

        fun resume(vararg registrations: CSRegistration) {
            registrations.onEach { it.resume() }
        }

        fun pause(vararg registrations: CSRegistration) {
            registrations.onEach { it.pause() }
        }

        fun CSRegistration(
            isActive: Boolean = false,
            onResume: ArgFun<CSRegistration>? = null,
            onPause: ArgFun<CSRegistration>? = null,
            onCancel: ArgFun<CSRegistration>? = null,
        ) = object : CSRegistrationImpl(isActive) {
            override fun onResume() {
                super.onResume()
                onResume?.invoke(this)
            }

            override fun onPause() {
                super.onPause()
                onPause?.invoke(this)
            }

            override fun onCancel() {
                super.onCancel()
                onCancel?.invoke(this)
            }

        }
    }

    override fun close() = cancel()
}
