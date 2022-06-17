package renetik.android.framework.task

import renetik.android.core.lang.CSMainHandler.postOnMain
import renetik.android.core.lang.CSMainHandler.removePosted


@Deprecated("Use CSVisibility schedule or other options")
class CSWork(private var interval: Int, private val function: (CSWork) -> Unit) {

    private var _isStarted = false

    val isStarted get() = _isStarted

    fun run() = apply { function(this) }

    fun start() = apply {
        if (!_isStarted) {
            _isStarted = true
            postOnMain(interval, ::runFunction)
        }
    }

    fun stop() = apply {
        _isStarted = false
        removePosted(::runFunction)
    }

    private fun runFunction() {
        if (this._isStarted) {
            function(this)
            postOnMain(interval, ::runFunction)
        }
    }

    fun start(start: Boolean) = apply { if (start) start() else stop() }

    fun interval(interval: Int) = apply { this.interval = interval }
}
