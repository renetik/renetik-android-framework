@file:Suppress("NOTHING_TO_INLINE")

package renetik.android.core.kotlin

inline fun Any?.equalsAny(vararg items: Any?): Boolean = this in items
inline infix fun Any?.equalsAny(items: Iterable<Any?>): Boolean = this in items

inline fun Any?.equalsNone(vararg items: Any?): Boolean = this !in items
inline infix fun Any?.equalsNone(items: Iterable<Any?>) = this !in items

inline fun Any?.equalsAll(vararg items: Any?): Boolean = items.all { this == it }
inline infix fun Any?.equalsAll(items: Iterable<Any?>): Boolean = items.all { this == it }