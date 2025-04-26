package renetik.android.ui.protocol

import renetik.android.event.registration.CSHasChangeValue

interface CSVisibility {
    //TODO: Rename to isVisibility to avoid platform clash
    val isVisible: CSHasChangeValue<Boolean>
    fun updateVisibility() = Unit
}