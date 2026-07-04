package renetik.android.event.change

import renetik.android.core.lang.synchronized

class CSValueFunction<Return>(
    val parent: Any,
    var value: Return,
    val function: (Return) -> Unit,
) {
    operator fun invoke(newValue: Return) = synchronized(parent) {
        if (value != newValue) {
            value = newValue
            function(newValue)
        }
    }

    inline fun update(produce: () -> Return) = synchronized(parent) {
        val newValue = produce()
        if (value != newValue) {
            value = newValue; function(newValue)
        }
    }
}