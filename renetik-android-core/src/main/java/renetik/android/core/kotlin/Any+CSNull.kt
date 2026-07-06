package renetik.android.core.kotlin

fun isAnyNotNull(vararg items: Any?) = items.any { it != null }

fun isAllNotNull(vararg items: Any?) = items.all { it != null }

fun isAnyNull(vararg items: Any?) = items.any { it == null }

fun isAllNull(vararg items: Any?): Boolean = items.all { it == null }