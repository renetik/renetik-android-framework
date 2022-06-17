package renetik.android.event

interface CSEventListener<T> : CSRegistration {
    fun onEvent(argument: T)
}