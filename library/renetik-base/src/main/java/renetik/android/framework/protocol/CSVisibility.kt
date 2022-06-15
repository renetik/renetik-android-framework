package renetik.android.framework.protocol

import renetik.android.framework.event.CSEvent

interface CSVisibility {
    fun updateVisibility()
    val isVisible: Boolean
    val eventVisibility: CSEvent<Boolean>
}