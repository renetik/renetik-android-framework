package renetik.android.event.registration

fun <T : CSRegistration> Array<T>.pause() = onEach { it.pause() }

fun <T : CSRegistration> Array<T>.resume() = onEach { it.resume() }
