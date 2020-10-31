package renetik.android.controller.extensions

import renetik.android.controller.common.CSNavigationInstance.navigation
import renetik.android.java.common.CSCondition
import renetik.android.java.common.CSCondition.Factory.condition
import renetik.android.java.event.CSEventProperty
import renetik.android.java.extensions.primitives.isTrue
import renetik.android.view.extensions.shownIf

fun validate(conditions: (CSPropertyConditionList).() -> Unit, onResult: (Boolean) -> Unit) {
    val dependency = CSPropertyConditionList { onResult(falseIfAnyConditionIsFalse()) }
    conditions(dependency)
    dependency.evaluate()
}

fun <View : android.view.View> View.shownIf(conditions: (CSPropertyConditionList).() -> Unit) =
    apply {
        validate(conditions) { result -> shownIf(result) }
    }

class CSPropertyConditionList(val evaluate: (CSPropertyConditionList).() -> Unit) {
    val conditions = mutableListOf<CSCondition>()

    fun <T> on(property: CSEventProperty<T>, condition: (T?) -> Boolean?) {
        conditions.add(CSPropertyCondition(property, condition))
        navigation.register(property.onChange { evaluate() })
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

class CSPropertyCondition<T>(val property: CSEventProperty<T>,
                             val condition: (T?) -> Boolean?) : CSCondition {
    override fun evaluate() = condition(property.value)
}