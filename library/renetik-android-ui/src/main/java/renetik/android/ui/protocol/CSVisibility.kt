package renetik.android.ui.protocol

import renetik.android.event.registration.CSHasChangeValue

interface CSVisibility {
    val isVisible: CSHasChangeValue<Boolean>
    fun updateVisibility() = Unit
}