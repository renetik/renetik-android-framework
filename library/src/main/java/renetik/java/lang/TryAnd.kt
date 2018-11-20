package renetik.java.lang

import kotlin.reflect.KClass
import renetik.android.lang.CSLog.logError
import renetik.android.lang.CSLog.logWarn

fun <ReturnType, ExceptionType : Throwable> tryAndWarn(
        type: KClass<ExceptionType>, function: () -> ReturnType): ReturnType? {
    try {
        return function()
    } catch (e: Throwable) {
        if (type.java.isInstance(e)) {
            logWarn(e)
            return null
        } else throw e
    }
}

fun <ReturnType> tryAndWarn(function: () -> ReturnType) = tryAndWarn(Exception::class, function)

fun <ReturnType, ExceptionType : Throwable> tryAndError(
        type: KClass<ExceptionType>, function: () -> ReturnType): ReturnType? {
    try {
        return function()
    } catch (e: Throwable) {
        if (type.java.isInstance(e)) {
            logError(e)
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

fun <ReturnType, ExceptionType : Throwable> tryAndCatch(type: KClass<ExceptionType>, function: () -> ReturnType, catch: () -> Unit): ReturnType? {
    try {
        return function()
    } catch (e: Throwable) {
        if (type.java.isInstance(e)) {
            catch()
            return null
        } else throw e
    }
}