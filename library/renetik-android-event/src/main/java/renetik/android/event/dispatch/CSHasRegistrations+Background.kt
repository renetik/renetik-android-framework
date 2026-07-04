package renetik.android.event.dispatch

//@Deprecated("Use launch")
//@AnyThread
//inline fun CSHasRegistrations.registerBackground(
//    @WorkerThread crossinline function: Func
//) = registerBackground(after = 0, function)
//
//@Deprecated("Use launch")
//@AnyThread
//inline fun CSHasRegistrations.registerBackground(
//    after: Int, @WorkerThread crossinline function: () -> Unit,
//): CSRegistration {
//    var registration: CSRegistration? = null
//    return (this + background(after) {
//        function()
//        registration?.cancel()
//    }).also { registration = it }
//}