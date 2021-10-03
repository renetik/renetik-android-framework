package renetik.android.framework.event

class CSMultiEventRegistration(
    private vararg val registrations: CSEventRegistration) :
    CSEventRegistration {
    override var isActive = true
    override fun cancel() {
        isActive = false
        registrations.forEach { it.cancel() }
    }
}