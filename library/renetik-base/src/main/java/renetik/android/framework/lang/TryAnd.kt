package renetik.android.framework.common

import renetik.android.framework.Func
import renetik.android.framework.logging.CSLog.logError
import renetik.android.framework.logging.CSLog.logWarn

inline fun <ReturnType> catchReturn(
    tryFunction: () -> ReturnType, onExceptionReturn: (Throwable) -> ReturnType
): ReturnType = try {
    tryFunction()
} catch (e: Throwable) {
    onExceptionReturn(e)
}

inline fun <ReturnType> catchReturnNull(
    tryFunction: () -> ReturnType): ReturnType? = catchReturn(tryFunction, { null })


inline fun <reified ExceptionType : Throwable, ReturnType> catchWarnReturn(
    message: String? = null,
    tryFunction: () -> ReturnType, onExceptionReturn: (ExceptionType) -> ReturnType
): ReturnType {
    return try {
        tryFunction()
    } catch (e: Throwable) {
        if (e is ExceptionType) {
            logWarn(e, message)
            onExceptionReturn(e)
        } else throw e
    }
}

inline fun <ReturnType, reified ExceptionType : Throwable> catchWarnReturn(
    onExceptionReturn: ReturnType, tryFunction: () -> ReturnType
) = catchWarnReturn<ExceptionType, ReturnType>(message = null, tryFunction, { onExceptionReturn })

inline fun <reified ExceptionType : Throwable> catchWarn(tryFunction: () -> Unit) =
    catchWarnReturn<Unit, ExceptionType>(Unit, tryFunction)

inline fun <ReturnType> catchAllWarnReturn(
    onExceptionReturn: ReturnType, tryFunction: () -> ReturnType
) = catchWarnReturn<Exception, ReturnType>(message = null, tryFunction, { onExceptionReturn })

inline fun catchAllWarn(tryFunction: () -> Unit) = catchAllWarnReturn(Unit, tryFunction)

inline fun <ReturnType, reified ExceptionType : Throwable> catchWarnReturnNull(
    tryFunction: () -> ReturnType
): ReturnType? = catchWarnReturn<ExceptionType, ReturnType?>(message = null, tryFunction, { null })


inline fun <ReturnType> catchAllWarnReturnNull(message: String? = null,
                                               tryFunction: () -> ReturnType)
        : ReturnType? = catchWarnReturn<Exception, ReturnType?>(message, tryFunction, { null })

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

inline fun <reified ExceptionType : Throwable, ReturnType>
        catchErrorReturn(onExceptionReturn: ReturnType, tryFunction: () -> ReturnType) =
    catchErrorReturn<ReturnType, ExceptionType>(tryFunction, { onExceptionReturn })

inline fun <reified ExceptionType : Throwable> catchError(tryFunction: () -> Unit) =
    catchErrorReturn<ExceptionType, Unit>(Unit, tryFunction)


inline fun <ReturnType>
        catchAllErrorReturn(onExceptionReturn: ReturnType, tryFunction: () -> ReturnType) =
    catchErrorReturn<ReturnType, Exception>(tryFunction, { onExceptionReturn })

inline fun catchAllError(tryFunction: () -> Unit) = catchAllErrorReturn(Unit, tryFunction)

inline fun <reified ExceptionType : Throwable, ReturnType>
        catchErrorReturnNull(tryFunction: () -> ReturnType): ReturnType? =
    catchErrorReturn<ExceptionType, ReturnType?>(null, tryFunction)

inline fun <ReturnType> catchAllErrorReturnNull(tryFunction: () -> ReturnType)
        : ReturnType? = catchErrorReturn<ReturnType?, Exception>(tryFunction, { null })

fun <ReturnType> tryAndFinally(tryFunction: () -> ReturnType, finally: () -> Unit): ReturnType {
    try {
        return tryFunction()
    } finally {
        finally()
    }
}

inline fun <reified ExceptionType : Throwable> catchIgnore(tryFunction: Func) = try {
    tryFunction()
} catch (e: Throwable) {
}

inline fun catchAllIgnore(tryFunction: Func) = catchIgnore<Throwable>(tryFunction)