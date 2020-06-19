package renetik.android.task

class CSDoLaterOnce(val function: () -> Unit) {
    private var willInvoke = false

    fun invoke() {
        if (willInvoke) return
        willInvoke = true
        later {
            function()
            willInvoke = false
        }
    }
}