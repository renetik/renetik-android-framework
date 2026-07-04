package renetik.android.event.registration

fun MutableMap<String, CSRegistration>.cancelRegistrations() = apply {
    values.onEach(CSRegistration::cancel)
}.clear()