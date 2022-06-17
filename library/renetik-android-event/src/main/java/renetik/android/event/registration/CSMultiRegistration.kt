package renetik.android.event.registration

class CSMultiRegistration(
    private vararg val registrations: CSRegistration) : CSRegistration {
    override var isActive = true
    override fun cancel() {
        isActive = false
        registrations.forEach(CSRegistration::cancel)
    }
}