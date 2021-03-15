package renetik.android.java

typealias Func = () -> Unit
typealias ArgFunc<Type> = (Type) -> Unit
typealias GetFunc<Type> = () -> Type
typealias ArgGetFunc<Arg, Get> = (Arg) -> Get