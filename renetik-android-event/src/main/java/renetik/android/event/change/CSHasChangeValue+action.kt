package renetik.android.event.change

import renetik.android.core.lang.tuples.CSQuadruple
import renetik.android.core.lang.tuples.CSQuintuple
import renetik.android.core.lang.tuples.CSSeventuple
import renetik.android.core.lang.tuples.CSSixtuple
import renetik.android.event.registration.CSRegistration

inline fun <Argument1, Argument2> Pair<CSHasChangeValue<out Argument1>,
        CSHasChangeValue<out Argument2>>.action(
    crossinline onAction: (Argument1, Argument2) -> Unit,
): CSRegistration = listOf(first, second).action {
    onAction(first.value, second.value)
}

fun <Argument1, Argument2> Pair<CSHasChangeValue<out Argument1>,
        CSHasChangeValue<Argument2>>.action(
    onAction: () -> Unit,
): CSRegistration = action { _, _ -> onAction() }

fun <Argument1, Argument2, Argument3>
        Triple<CSHasChangeValue<Argument1>,
                CSHasChangeValue<Argument2>,
                CSHasChangeValue<Argument3>>.action(
    onAction: (Argument1, Argument2, Argument3) -> Unit,
): CSRegistration = listOf(first, second, third).action {
    onAction(first.value, second.value, third.value)
}

fun <Argument1, Argument2, Argument3, Argument4>
        CSQuadruple<CSHasChangeValue<Argument1>,
                CSHasChangeValue<Argument2>, CSHasChangeValue<Argument3>,
                CSHasChangeValue<Argument4>>.action(
    onAction: (Argument1, Argument2, Argument3, Argument4) -> Unit,
): CSRegistration = listOf(first, second, third, fourth).action {
    onAction(first.value, second.value, third.value, fourth.value)
}

fun <Argument1, Argument2, Argument3, Argument4, Argument5>
        CSQuintuple<CSHasChangeValue<Argument1>, CSHasChangeValue<Argument2>,
                CSHasChangeValue<Argument3>, CSHasChangeValue<Argument4>,
                CSHasChangeValue<Argument5>>.action(
    onAction: (Argument1, Argument2, Argument3, Argument4, Argument5) -> Unit,
): CSRegistration = listOf(first, second, third, fourth, fifth).action {
    onAction(first.value, second.value, third.value, fourth.value, fifth.value)
}

fun <Argument1, Argument2, Argument3, Argument4, Argument5, Argument6>
        CSSixtuple<CSHasChangeValue<Argument1>, CSHasChangeValue<Argument2>,
                CSHasChangeValue<Argument3>, CSHasChangeValue<Argument4>,
                CSHasChangeValue<Argument5>, CSHasChangeValue<Argument6>
                >.action(
    onAction: (Argument1, Argument2, Argument3, Argument4, Argument5, Argument6) -> Unit,
): CSRegistration = listOf(first, second, third, fourth, fifth, sixth).action {
    onAction(first.value, second.value, third.value, fourth.value, fifth.value, sixth.value)
}

fun <Argument1, Argument2, Argument3, Argument4, Argument5, Argument6, Argument7>
        CSSeventuple<CSHasChangeValue<Argument1>, CSHasChangeValue<Argument2>,
                CSHasChangeValue<Argument3>, CSHasChangeValue<Argument4>,
                CSHasChangeValue<Argument5>, CSHasChangeValue<Argument6>,
                CSHasChangeValue<Argument7>>.action(
    onAction: (Argument1, Argument2, Argument3, Argument4,
               Argument5, Argument6, Argument7) -> Unit,
): CSRegistration = listOf(first, second, third, fourth,
    fifth, sixth, seventh).action {
    onAction(first.value, second.value, third.value,
        fourth.value, fifth.value, sixth.value, seventh.value)
}