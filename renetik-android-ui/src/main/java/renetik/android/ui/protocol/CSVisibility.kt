package renetik.android.ui.protocol

import renetik.android.event.change.CSHasChangeValue

interface CSVisibility {
    val isVisibility: CSHasChangeValue<Boolean>
    fun updateVisibility() = Unit
}