package renetik.android.framework.event

interface CSEventRegistration {
    var isActive: Boolean
    fun cancel() {
        isActive = false
    }

    companion object {
        fun registration(onCancel: () -> Unit) = object : CSEventRegistration {
            override var isActive = true
            override fun cancel() {
                super.cancel()
                onCancel()
            }
        }
    }
}