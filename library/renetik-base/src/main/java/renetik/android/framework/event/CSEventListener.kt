package renetik.android.framework.event

interface CSEventListener<T> : CSRegistration {
    fun onEvent(argument: T)
}