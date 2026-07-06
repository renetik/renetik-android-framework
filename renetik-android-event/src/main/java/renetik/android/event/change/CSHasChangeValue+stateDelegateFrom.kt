@file:Suppress("NOTHING_TO_INLINE")

package renetik.android.event.change

import renetik.android.core.lang.ArgFun
import renetik.android.core.lang.tuples.CSQuadruple
import renetik.android.core.lang.tuples.CSQuintuple
import renetik.android.core.lang.tuples.CSSixtuple
import renetik.android.core.lang.tuples.to
import renetik.android.event.registration.CSHasRegistrations

fun <Argument, Return> CSHasChangeValue<Argument>.stateDelegate(
    parent: CSHasRegistrations? = null,
    fromValue: (Argument) -> Return,
    onChange: ArgFun<Return>? = null
): CSHasChangeValue<Return> = let { source ->
    object : CSHasChangeValueBase<Return>(parent, onChange) {
        override var value: Return = fromValue(source.value)

        init {
            this + source.onChange { value(fromValue(it)) }
        }
    }
}

fun <Argument, Return> CSHasChangeValue<Argument>.stateDelegate(
    parent: CSHasRegistrations? = null,
    fromValueAndPrevious: (Argument, Return?) -> Return,
    onChange: ArgFun<Return>? = null
): CSHasChangeValue<Return> =
    let { property ->
        object : CSHasChangeValueBase<Return>(parent, onChange) {
            var previous: Return? = null

            override var value: Return = fromValueAndPrevious(property.value, previous)

            init {
                this + property.onChange {
                    value(fromValueAndPrevious(it, previous))
                    previous = value
                }
            }
        }
    }

fun <Argument1, Argument2, Return>
        Pair<CSHasChangeValue<Argument1>,
                CSHasChangeValue<Argument2>>.stateDelegate(
    parent: CSHasRegistrations? = null,
    fromValues: (Argument1, Argument2) -> Return,
    onChange: ArgFun<Return>? = null
): CSHasChangeValue<Return> =
    object : CSHasChangeValueBase<Return>(parent, onChange) {
        override var value: Return = fromValues(first.value, second.value)

        init {
            this + (first to second).onChange { item1, item2 ->
                value(fromValues(item1, item2))
            }
        }
    }

fun <Argument1, Argument2, Argument3, Return>
        Triple<CSHasChangeValue<Argument1>,
                CSHasChangeValue<Argument2>,
                CSHasChangeValue<Argument3>>.stateDelegate(
    parent: CSHasRegistrations? = null,
    fromValues: (Argument1, Argument2, Argument3) -> Return,
    onChange: ArgFun<Return>? = null
): CSHasChangeValue<Return> =
    object : CSHasChangeValueBase<Return>(parent, onChange) {
        override var value: Return = fromValues(first.value, second.value, third.value)

        init {
            this + (first to second to third).onChange { item1, item2, item3 ->
                value(fromValues(item1, item2, item3))
            }
        }
    }

fun <Argument1, Argument2, Return>
        Triple<CSHasChangeValue<Argument1>,
                CSHasChangeValue<Argument2>,
                CSHasChange<*>>.stateDelegate(
    parent: CSHasRegistrations? = null,
    fromValues: (Argument1, Argument2) -> Return,
    onChange: ArgFun<Return>? = null
): CSHasChangeValue<Return> =
    object : CSHasChangeValueBase<Return>(parent, onChange) {
        override var value: Return = fromValues(first.value, second.value)

        init {
            this + (first to second to third).onChange { item1, item2 ->
                value(fromValues(item1, item2))
            }
        }
    }


fun <Argument1, Argument2, Argument3, Argument4, Return>
        CSQuadruple<CSHasChangeValue<Argument1>,
                CSHasChangeValue<Argument2>, CSHasChangeValue<Argument3>,
                CSHasChangeValue<Argument4>>.stateDelegate(
    parent: CSHasRegistrations? = null,
    fromValues: (Argument1, Argument2, Argument3, Argument4) -> Return,
    onChange: ArgFun<Return>? = null
): CSHasChangeValue<Return> =
    object : CSHasChangeValueBase<Return>(parent, onChange) {
        override var value: Return =
            fromValues(first.value, second.value, third.value, fourth.value)

        init {
            this + (first to second to third to fourth)
                .onChange { item1, item2, item3, item4 ->
                    value(fromValues(item1, item2, item3, item4))
                }
        }
    }

fun <Argument1, Argument2, Argument3, Argument4, Return>
        CSQuadruple<CSHasChangeValue<Argument1>,
                CSHasChangeValue<Argument2>, CSHasChangeValue<Argument3>,
                CSHasChangeValue<Argument4>>.stateDelegate(
    parent: CSHasRegistrations? = null,
    fromValues: (CSQuadruple<Argument1, Argument2, Argument3, Argument4>) -> Return,
    onChange: ArgFun<Return>? = null
): CSHasChangeValue<Return> =
    object : CSHasChangeValueBase<Return>(parent, onChange) {
        override var value: Return =
            fromValues(CSQuadruple(first.value, second.value, third.value, fourth.value))

        init {
            this + (first to second to third to fourth)
                .onChange { item1, item2, item3, item4 ->
                    value(fromValues(CSQuadruple(item1, item2, item3, item4)))
                }
        }
    }

fun <Argument1, Argument2, Argument3, Argument4, Argument5, Return>
        CSQuintuple<CSHasChangeValue<Argument1>, CSHasChangeValue<Argument2>,
                CSHasChangeValue<Argument3>, CSHasChangeValue<Argument4>,
                CSHasChangeValue<Argument5>>.stateDelegate(
    parent: CSHasRegistrations? = null,
    fromValues: (Argument1, Argument2, Argument3, Argument4, Argument5) -> Return,
    onChange: ArgFun<Return>? = null
): CSHasChangeValue<Return> =
    object : CSHasChangeValueBase<Return>(parent, onChange) {
        override var value: Return =
            fromValues(first.value, second.value, third.value, fourth.value, fifth.value)

        init {
            this + (first to second to third to fourth to fifth).onChange { item1, item2, item3, item4, item5 ->
                value(fromValues(item1, item2, item3, item4, item5))
            }
        }
    }

fun <Argument1, Argument2, Argument3, Argument4, Argument5>
        CSQuintuple<CSHasChangeValue<Argument1>, CSHasChangeValue<Argument2>,
                CSHasChangeValue<Argument3>, CSHasChangeValue<Argument4>,
                CSHasChangeValue<Argument5>>.stateDelegate(
    parent: CSHasRegistrations? = null,
    onChange: ArgFun<CSQuintuple<Argument1, Argument2, Argument3, Argument4, Argument5>>? = null
): CSHasChangeValue<CSQuintuple<Argument1, Argument2, Argument3, Argument4, Argument5>> =
    stateDelegate(parent, fromValues = { item1, item2, item3, item4, item5 ->
        CSQuintuple(item1, item2, item3, item4, item5)
    }, onChange)

fun <Argument1, Argument2, Argument3, Argument4, Argument5, Argument6, Return>
        CSSixtuple<CSHasChangeValue<Argument1>, CSHasChangeValue<Argument2>,
                CSHasChangeValue<Argument3>, CSHasChangeValue<Argument4>,
                CSHasChangeValue<Argument5>, CSHasChangeValue<Argument6>>.stateDelegate(
    parent: CSHasRegistrations? = null,
    fromValues: (Argument1, Argument2, Argument3, Argument4, Argument5, Argument6) -> Return,
    onChange: ArgFun<Return>? = null
): CSHasChangeValue<Return> =
    object : CSHasChangeValueBase<Return>(parent, onChange) {
        override var value: Return = fromValues(first.value, second.value,
            third.value, fourth.value, fifth.value, sixth.value)

        init {
            this + (first to second to third to fourth to fifth to sixth)
                .onChange { item1, item2, item3, item4, item5, item6 ->
                    value(fromValues(item1, item2, item3, item4, item5, item6))
                }
        }
    }

