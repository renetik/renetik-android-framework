package renetik.android.java.common

import renetik.android.logging.CSLog.logError
import renetik.android.logging.CSLog.logWarn


inline fun <ReturnType> catchReturn(
    tryFunction: () -> ReturnType, onExceptionReturn: (Throwable) -> ReturnType
): ReturnType = try {
    tryFunction()
} catch (e: Throwable) {
    onExceptionReturn(e)
}

inline fun <ReturnType> catchReturnNull(
    tryFunction: () -> ReturnType): ReturnType? = catchReturn(tryFunction, { null })

inline fun <ReturnType, reified ExceptionType : Throwable> catchWarnReturn(
    tryFunction: () -> ReturnType, onExceptionReturn: (ExceptionType) -> ReturnType
): ReturnType {
    return try {
        tryFunction()
    } catch (e: Throwable) {
        if (e is ExceptionType) {
            logWarn(e)
            onExceptionReturn(e)
        } else throw e
    }
}

inline fun <ReturnType, reified ExceptionType : Throwable> catchWarnReturn(
    onExceptionReturn: ReturnType, tryFunction: () -> ReturnType
) = catchWarnReturn<ReturnType, ExceptionType>(tryFunction, { onExceptionReturn })

inline fun <reified ExceptionType : Throwable> catchWarn(tryFunction: () -> Unit) =
    catchWarnReturn<Unit, ExceptionType>(Unit, tryFunction)

inline fun <ReturnType> catchAllWarnReturn(
    onExceptionReturn: ReturnType, tryFunction: () -> ReturnType
) = catchWarnReturn<ReturnType, Exception>(tryFunction, { onExceptionReturn })

inline fun catchAllWarn(tryFunction: () -> Unit) = catchAllWarnReturn(Unit, tryFunction)

inline fun <ReturnType, reified ExceptionType : Throwable> catchWarnReturnNull(
    tryFunction: () -> ReturnType
): ReturnType? = catchWarnReturn<ReturnType?, ExceptionType>(tryFunction, { null })


inline fun <ReturnType> catchAllWarnReturnNull(tryFunction: () -> ReturnType)
        : ReturnType? = catchWarnReturn<ReturnType?, Exception>(tryFunction, { null })

inline fun <ReturnType, reified ExceptionType : Throwable> catchErrorReturn(
    tryFunction: () -> ReturnType, onExceptionReturn: (ExceptionType) -> ReturnType
): ReturnType {
    return try {
        tryFunction()
    } catch (e: Throwable) {
        if (e is ExceptionType) {
            logError(e)
            onExceptionReturn(e)
        } else throw e
    }
}

inline fun <ReturnType, reified ExceptionType : Throwable> catchErrorReturn(
    onExceptionReturn: ReturnType, tryFunction: () -> ReturnType
) = catchErrorReturn<ReturnType, ExceptionType>(tryFunction, { onExceptionReturn })

inline fun <reified ExceptionType : Throwable> catchError(tryFunction: () -> Unit) =
    catchErrorReturn<Unit, ExceptionType>(Unit, tryFunction)

inline fun <ReturnType> catchAllErrorReturn(
    onExceptionReturn: ReturnType, tryFunction: () -> ReturnType
) = catchErrorReturn<ReturnType, Exception>(tryFunction, { onExceptionReturn })

inline fun catchAllError(tryFunction: () -> Unit) = catchAllErrorReturn(Unit, tryFunction)

inline fun <ReturnType, reified ExceptionType : Throwable> catchErrorReturnNull(
    tryFunction: () -> ReturnType
): ReturnType? = catchWarnReturn<ReturnType?, ExceptionType>(tryFunction, { null })

inline fun <ReturnType> catchAllErrorReturnNull(tryFunction: () -> ReturnType)
        : ReturnType? = catchErrorReturn<ReturnType?, Exception>(tryFunction, { null })

fun <ReturnType> tryAndFinally(tryFunction: () -> ReturnType, finally: () -> Unit): ReturnType {
    try {
        return tryFunction()
    } finally {
        finally()
    }
}