package renetik.android.framework.event

class CSMultiEventRegistration(
    private vararg val registrations: CSRegistration) : CSRegistration {
    override var isActive = true
    override fun cancel() {
        isActive = false
        registrations.forEach(CSRegistration::cancel)
    }
}