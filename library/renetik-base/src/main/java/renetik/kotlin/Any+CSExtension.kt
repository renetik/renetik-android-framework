package renetik.kotlin

import renetik.android.framework.event.CSEventRegistration
import renetik.android.framework.lang.CSHasId
import renetik.android.framework.util.CSMainHandler.postOnMain
import renetik.java.lang.isMain
import kotlin.properties.ObservableProperty
import kotlin.reflect.KProperty

inline fun later(delayMilliseconds: Int = 0, crossinline function: () -> Unit) =
    object : CSEventRegistration {
        init {
            postOnMain(delayMilliseconds) {
                if (isActive) {
                    isActive = false
                    function()
                }
            }
        }

        override var isActive = true
        override fun cancel() {
            isActive = false
        }
    }

inline fun later(crossinline function: () -> Unit) = later(0, function)

fun <T : Any> T.onMainThread(function: (T).() -> Unit) {
    if (Thread.currentThread().isMain) function()
    else postOnMain { function(this) }
}

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