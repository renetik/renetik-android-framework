package renetik.android.framework.event

interface CSVisibility {
    fun updateVisibility()
    val isVisible: Boolean
    val eventVisibility: CSEvent<Boolean>
}