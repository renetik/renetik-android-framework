package renetik.android.framework.event

interface CSHasDestroy {
    val eventDestroy: CSEvent<Unit>
    fun onDestroy()

    companion object {
        fun CSHasDestroy.onDestroy(listener: () -> Unit) = eventDestroy.listenOnce { listener() }
    }
}