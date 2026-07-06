@file:Suppress("NOTHING_TO_INLINE")

package renetik.android.core.logging

inline fun CSLogger.isEnabled(level: CSLogLevel) = !isDisabled(level)

inline fun CSLogger.isDisabled(level: CSLogLevel) = level.ordinal < this.level.ordinal