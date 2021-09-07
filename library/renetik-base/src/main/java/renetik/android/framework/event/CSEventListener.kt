package renetik.android.framework.event

interface CSEventListener<T> : CSEventRegistration {
    fun onEvent(argument: T)
}