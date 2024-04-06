package renetik.android.controller.extensions

import renetik.android.event.registration.CSRegistration
import renetik.android.event.property.CSProperty
import renetik.android.core.lang.CSCondition
import renetik.android.core.lang.CSCondition.Factory.condition
import renetik.android.core.kotlin.primitives.isTrue
import renetik.android.ui.extensions.view.show
import renetik.android.ui.extensions.view.superview

fun validate(conditions: (CSPropertyConditionList).() -> Unit, onResult: (Boolean) -> Unit) {
    val dependency = CSPropertyConditionList { onResult(falseIfAnyConditionIsFalse()) }
    conditions(dependency)
    dependency.evaluate()
}

fun <View : android.view.View> View.shownIf(
    conditions: CSPropertyConditionList.() -> Unit, isInContainer: Boolean = false
) = apply {
    validate(conditions) { result ->
        if (isInContainer) superview!!.show(result)
        else show(result)
    }
}

class CSPropertyConditionList(val evaluate: (CSPropertyConditionList).() -> Unit) {
    val conditions = mutableListOf<CSCondition>()

    fun <T> on(property: CSProperty<T>, condition: (T?) -> Boolean?): CSRegistration {
        conditions.add(CSPropertyCondition(property, condition))
        return property.onChange { evaluate() }
    }

    fun on(condition: () -> Boolean?) {
        conditions.add(condition(condition))
    }

    fun evaluate() = evaluate(this)
}

private fun CSPropertyConditionList.falseIfAnyConditionIsFalse(): Boolean {
    var result = true
    conditions.forEach { if (!it.evaluate().isTrue) result = false }
    return result
}

class CSPropertyCondition<T>(val property: CSProperty<T>,
                             val condition: (T?) -> Boolean?) : CSCondition {
    override fun evaluate() = condition(property.value)
}