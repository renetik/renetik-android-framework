package renetik.android.framework

typealias void = Unit
typealias Func = () -> void
typealias ArgFunc<Type> = (Type) -> void
typealias GetFunc<Type> = () -> Type
typealias ArgGetFunc<Arg, Get> = (Arg) -> Get