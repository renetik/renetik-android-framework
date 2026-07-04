package renetik.android.core.lang

typealias Fun = () -> Unit
typealias SusFun = suspend () -> Unit
typealias ArgFun<Arg> = (Arg) -> Unit
typealias SusArgFun<Arg> = suspend (Arg) -> Unit
typealias ArgArgFun<Arg1, Arg2> = (Arg1, Arg2) -> Unit
typealias ReturnFun<Return> = () -> Return
typealias ArgReturnFun<Arg, Return> = (Arg) -> Return

operator fun <T> ArgFun<T>?.invoke(type: T) {
    this?.invoke(type)
}