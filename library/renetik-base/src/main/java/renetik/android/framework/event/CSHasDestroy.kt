package renetik.android.framework.event

interface CSHasDestroy {
    val eventDestroy: CSEvent<Unit>
    fun onDestroy()
}

fun CSHasDestroy.onDestroy(listener: () -> Unit) = eventDestroy.listenOnce { listener() }