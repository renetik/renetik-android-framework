package renetik.java.lang

import renetik.android.lang.CSLog.logError
import renetik.android.lang.CSLog.logWarn
import kotlin.reflect.KClass

fun <ReturnType, ExceptionType : Throwable> tryAndWarn(
        type: KClass<ExceptionType>, function: () -> ReturnType): ReturnType? {
    return try {
        function()
    } catch (e: Throwable) {
        if (type.java.isInstance(e)) {
            logWarn(e)
            null
        } else throw e
    }
}

fun <ReturnType> tryAndWarn(function: () -> ReturnType) = tryAndWarn(Exception::class, function)

fun <ReturnType, ExceptionType : Throwable> tryAndError(
        type: KClass<ExceptionType>, function: () -> ReturnType): ReturnType? {
    return try {
        function()
    } catch (e: Throwable) {
        if (type.java.isInstance(e)) {
            logError(e)
            null
        } else throw e
    }
}

fun <ReturnType> tryAndError(function: () -> ReturnType) = tryAndError(Exception::class, function)

fun <ReturnType> tryAndFinally(function: () -> ReturnType, finally: () -> Unit): ReturnType {
    try {
        return function()
    } finally {
        finally()
    }
}

fun <ReturnType, ExceptionType : Throwable> tryAndCatch(type: KClass<ExceptionType>, function: () -> ReturnType, onException: () -> ReturnType): ReturnType {
    return try {
        function()
    } catch (e: Throwable) {
        if (type.java.isInstance(e))
            onException.invoke()
        else throw e
    }
}