package renetik.android.core.lang

typealias void = Unit
typealias Func = () -> void
typealias ArgFunc<Arg> = (Arg) -> void
typealias ReturnFunc<Return> = () -> Return
typealias ArgReturnFunc<Arg, Return> = (Arg) -> Return