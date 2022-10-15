package renetik.android.ui.protocol

import renetik.android.event.CSEvent

interface CSVisibility {
    val isVisible: Boolean
    val eventVisibility: CSEvent<Boolean>
    fun updateVisibility()
}