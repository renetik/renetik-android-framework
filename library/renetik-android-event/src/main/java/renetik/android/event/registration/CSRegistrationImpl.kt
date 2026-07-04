@file:OptIn(ExperimentalAtomicApi::class)

package renetik.android.event.registration

import androidx.annotation.AnyThread
import renetik.android.core.logging.CSLog.logWarnTrace
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.fire
import kotlin.concurrent.atomics.AtomicBoolean
import kotlin.concurrent.atomics.ExperimentalAtomicApi

open class CSRegistrationImpl(
    isActive: Boolean = false
) : CSRegistration {

    private val _isActive = AtomicBoolean(isActive)
    final override val isActive: Boolean get() = _isActive.load()

    private val _isCanceled = AtomicBoolean(false)
    final override val isCanceled: Boolean get() = _isCanceled.load()

    override val eventCancel by lazy { event().onMain(this) }

    @AnyThread
    final override fun resume() {
        if (isCanceled) {
            logWarnTrace { "Already canceled:$this" }
            return
        }
        val previouslyActive = _isActive.exchange(true)
        if (!previouslyActive) {
            if (isCanceled) {
                _isActive.exchange(false)
                return
            }
            onResume()
        }
    }

    protected open fun onResume() = Unit

    @AnyThread
    final override fun pause() {
        if (isCanceled) {
            logWarnTrace { "Already canceled:$this" }
            return
        }
        if (_isActive.exchange(false)) onPause()
    }

    protected open fun onPause() = Unit

    @AnyThread
    final override fun cancel() {
        if (_isCanceled.exchange(true)) return
        if (_isActive.exchange(false)) onPause()
        onCancel()
        eventCancel.fire()
    }

    protected open fun onCancel() = Unit
}