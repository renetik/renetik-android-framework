package renetik.android.event.change

import renetik.android.core.lang.tuples.CSQuadruple
import renetik.android.core.lang.tuples.CSQuintuple
import renetik.android.core.lang.tuples.CSSixtuple
import renetik.android.event.registration.CSRegistration

fun <Argument1, Argument2> Pair<CSHasChangeValue<Argument1>,
        CSHasChangeValue<Argument2>>.onChange(
    onChange: (Argument1, Argument2) -> Unit,
): CSRegistration = listOf(first, second).onChange {
    onChange(first.value, second.value)
}

fun <Argument1, Argument2> Pair<CSHasChangeValue<Argument1>,
        CSHasChangeValue<Argument2>>.onChange(
    onChange: () -> Unit,
): CSRegistration = listOf(first, second).onChange {
    onChange()
}

fun <Argument1, Argument2>
        Triple<CSHasChangeValue<Argument1>,
                CSHasChangeValue<Argument2>,
                CSHasChange<*>>.onChange(
    onChange: (Argument1, Argument2) -> Unit,
): CSRegistration = listOf(first, second, third).onChange {
    onChange(first.value, second.value)
}

fun <Argument1, Argument2, Argument3>
        Triple<CSHasChangeValue<Argument1>,
                CSHasChangeValue<Argument2>,
                CSHasChangeValue<Argument3>>.onChange(
    onChange: (Argument1, Argument2, Argument3) -> Unit,
): CSRegistration = listOf(first, second, third).onChange {
    onChange(first.value, second.value, third.value)
}

fun <Argument1, Argument2, Argument3>
        Triple<CSHasChangeValue<Argument1>,
                CSHasChangeValue<Argument2>,
                CSHasChangeValue<Argument3>>.onChange(
    onChange: () -> Unit,
): CSRegistration = listOf(first, second, third).onChange {
    onChange()
}

fun <Argument1, Argument2, Argument3, Argument4>
        CSQuadruple<CSHasChangeValue<Argument1>,
                CSHasChangeValue<Argument2>, CSHasChangeValue<Argument3>,
                CSHasChangeValue<Argument4>>.onChange(
    onChange: (Argument1, Argument2, Argument3, Argument4) -> Unit,
): CSRegistration = listOf(first, second, third, fourth).onChange {
    onChange(first.value, second.value, third.value, fourth.value)
}

fun <Argument1, Argument2, Argument3, Argument4, Argument5>
        CSQuintuple<CSHasChangeValue<Argument1>, CSHasChangeValue<Argument2>,
                CSHasChangeValue<Argument3>, CSHasChangeValue<Argument4>,
                CSHasChangeValue<Argument5>>.onChange(
    onChange: (Argument1, Argument2, Argument3, Argument4, Argument5) -> Unit,
): CSRegistration = listOf(first, second, third, fourth, fifth).onChange {
    onChange(first.value, second.value, third.value, fourth.value, fifth.value)
}

fun <Argument1, Argument2, Argument3, Argument4, Argument5, Argument6>
        CSSixtuple<CSHasChangeValue<Argument1>, CSHasChangeValue<Argument2>,
                CSHasChangeValue<Argument3>, CSHasChangeValue<Argument4>,
                CSHasChangeValue<Argument5>, CSHasChangeValue<Argument6>>.onChange(
    onChange: (Argument1, Argument2, Argument3, Argument4, Argument5, Argument6) -> Unit,
): CSRegistration = listOf(first, second, third, fourth, fifth, sixth).onChange {
    onChange(first.value, second.value, third.value, fourth.value, fifth.value, sixth.value)
}