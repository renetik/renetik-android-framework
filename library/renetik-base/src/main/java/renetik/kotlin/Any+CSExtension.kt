package renetik.kotlin

import renetik.android.framework.event.CSRegistration.Companion.registration
import renetik.android.framework.lang.CSHasId
import renetik.android.framework.util.CSMainHandler.postOnMain
import kotlin.properties.ObservableProperty
import kotlin.reflect.KProperty

inline fun later(delayMilliseconds: Int,
                 crossinline function: () -> Unit) =
    registration().also {
        postOnMain(delayMilliseconds) {
            if (it.isActive) {
                it.isActive = false
                function()
            }
        }
    }

/** Later uses default of 5 due to one strange rare multithreading issue
 *  where later function where executed earlier then later returned registration
 */
inline fun later(crossinline function: () -> Unit) = later(5, function)

fun <T : Any> T.runIf(condition: Boolean, function: (T) -> T) =
    if (condition) function(this) else this

//Interesting but I don't use it: by observing(...
fun <T> observing(value: T, willSet: (T) -> Unit = { }, didSet: (T) -> Unit = { }) =
    object : ObservableProperty<T>(value) {
        override fun beforeChange(property: KProperty<*>, oldValue: T, newValue: T): Boolean {
            willSet(newValue); return true
        }

        override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) =
            didSet(oldValue)
    }

val <T : Any> T.className get() = this::class.simpleName

fun Any?.toId() = (this as? CSHasId)?.id ?: toString()