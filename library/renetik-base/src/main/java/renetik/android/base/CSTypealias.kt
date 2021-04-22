package renetik.android.base

typealias void = Unit
typealias Func = () -> void
typealias ArgFunc<Type> = (Type) -> void
typealias GetFunc<Type> = () -> Type
typealias ArgGetFunc<Arg, Get> = (Arg) -> Get