package renetik.android.event.registration

fun <T : CSRegistration> List<T>.pause() = onEach { it.pause() }

fun <T : CSRegistration> List<T>.resume() = onEach { it.resume() }

fun MutableList<CSRegistration>.cancelRegistrations() = onEach(CSRegistration::cancel).clear()