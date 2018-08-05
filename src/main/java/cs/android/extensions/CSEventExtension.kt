package cs.android.extensions

import cs.java.collections.CSMap
import cs.java.event.CSEvent
import cs.java.event.CSEvent.CSEventRegistration
import cs.java.event.CSListener
import cs.java.lang.CSLang.NO
import cs.java.lang.CSLang.YES
import kotlin.reflect.KClass

fun <T> CSEvent<T>.execute(function: (argument:T) -> Unit): CSEventRegistration {
   return this.add { _, arg -> function(arg)}
}