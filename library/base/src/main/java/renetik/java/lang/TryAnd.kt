package renetik.java.lang

import renetik.android.logging.CSLog.logError
import renetik.android.logging.CSLog.logWarn
import renetik.java.extensions.isInstanceOf
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

fun <ReturnType, ExceptionType : Throwable> tryAndCatch(
        type: KClass<ExceptionType>, function: () -> ReturnType, onException: (ExceptionType) -> ReturnType): ReturnType {
    return try {
        function()
    } catch (throwable: Throwable) {
        @Suppress("UNCHECKED_CAST")
        if (throwable.isInstanceOf(type)) onException.invoke(throwable as ExceptionType)
        else throw throwable
    }
}

fun <ReturnType> tryAndCatch(function: () -> ReturnType, onException: (Exception) -> ReturnType) =
        tryAndCatch(Exception::class, function, onException)