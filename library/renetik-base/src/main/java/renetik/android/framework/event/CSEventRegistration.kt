package renetik.android.framework.event

interface CSEventRegistration {

    var isActive: Boolean

    fun cancel()
}