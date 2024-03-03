package renetik.android.ui.protocol

import renetik.android.event.CSEvent

interface CSVisibility {
    //TODO!!!! Why don't we have property isVisible:CSProperty<Boolean> ?
    val isVisible: Boolean
    val eventVisibility: CSEvent<Boolean>
    fun updateVisibility()
}