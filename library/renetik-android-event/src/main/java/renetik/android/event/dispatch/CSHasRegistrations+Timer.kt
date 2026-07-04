package renetik.android.event.dispatch

//import renetik.android.core.java.util.CSTimer

//inline fun CSTimer.schedule(
//    interval: Long, delay: Long = interval,
//    crossinline function: (CSRegistration) -> Unit): CSRegistration {
//    lateinit var scheduled: ScheduledFuture<*>
//    lateinit var registration: CSRegistration
//    registration = CSFunctionRegistration(
//        function = { function(registration) }, onCancel = { scheduled.cancel() })
//    scheduled = scheduleAtFixedRate(delay, interval, registration.function)
//    return registration
//}

//inline fun CSTimer.schedule(
//    count: Int, interval: Long, delay: Long = interval,
//    crossinline function: (index: Int) -> Unit,
//    crossinline onDone: Func = {}): CSRegistration {
//    var index = 0
//    return schedule(interval, delay) {
//        function(index)
//        index++
//        if (index == count) {
//            it.cancel()
//            onDone()
//        }
//    }
//}