package renetik.android.lang

import kotlin.reflect.KClass

fun <ReturnType, ExceptionType : Exception> tryAndWarn(
        type: KClass<ExceptionType>, function: () -> ReturnType): ReturnType? {
    try {
        return function()
    } catch (e: Exception) {
        if (type.java.isInstance(e)) {
            warn(e)
            return null
        } else throw e
    }
}

fun <ReturnType> tryAndWarn(function: () -> ReturnType) = tryAndWarn(Exception::class, function)

fun <ReturnType, ExceptionType : Exception> tryAndError(
        type: KClass<ExceptionType>, function: () -> ReturnType): ReturnType? {
    try {
        return function()
    } catch (e: Exception) {
        if (type.java.isInstance(e)) {
            error(e)
            return null
        } else throw e
    }
}

fun <ReturnType> tryAndError(function: () -> ReturnType) = tryAndError(Exception::class, function)

fun <ReturnType> tryAndFinally(function: () -> ReturnType, finnaly: () -> Unit): ReturnType {
    try {
        return function()
    } finally {
        finnaly()
    }
}