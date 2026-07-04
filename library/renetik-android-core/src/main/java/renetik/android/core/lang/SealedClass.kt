package renetik.android.core.lang

import kotlin.reflect.full.isSubclassOf

open class CSSealedClass {
    companion object {

        //  val values by lazy<List<LogLevel>>(::collectValues)
        inline fun <reified T : CSSealedClass> collectValues(): List<T> =
            T::class.nestedClasses
                .filter { type -> type.isSubclassOf(T::class) }
                .map { type -> type.objectInstance }
                .filterIsInstance<T>()
    }
}


///**
// * Defines severity of a log message.
// */
//sealed class LogLevel(internal val logKitLevel: LKLogLevel) : SealedClass() {
//    /**
//     * Disables logging.
//     */
//    data object Disabled : LogLevel(LKLogLevel.Disabled)
//
//    /**
//     * Log something generally unimportant (lowest priority).
//     */
//    data object Verbose : LogLevel(LKLogLevel.Verbose)
//
//    /**
//     * Log something which helps during debugging (low priority).
//     */
//    data object Debug : LogLevel(LKLogLevel.Debug)
//
//    /**
//     * Log something which you are interested in but which is not an issue or error (normal priority).
//     */
//    data object Info : LogLevel(LKLogLevel.Info)
//
//    /**
//     * Log something which may cause trouble soon (high priority).
//     */
//    data object Warning : LogLevel(LKLogLevel.Warning)
//
//    /**
//     * Log something which will keep you awake at night (highest priority).
//     */
//    data object Error : LogLevel(LKLogLevel.Error)
//
//    operator fun compareTo(other: LogLevel): Int =
//        logKitLevel.level.compareTo(other.logKitLevel.level)
//
//    val name: String = logKitLevel.name
//
//    companion object {
//        val values by lazy<List<LogLevel>>(::collectValues)
//    }
//}