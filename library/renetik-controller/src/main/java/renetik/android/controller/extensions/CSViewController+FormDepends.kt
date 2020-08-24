package renetik.android.controller.extensions

import renetik.android.controller.common.CSNavigationInstance.navigation
import renetik.android.java.event.CSEventProperty
import renetik.android.java.extensions.primitives.isTrue
import renetik.android.view.extensions.shownIf

fun <View : android.view.View, T : Any> View.depends(
    property: CSEventProperty<T?>, conditions: (CSPropertyConditionList).() -> Unit) = apply {
    val dependency = CSPropertyConditionList {
        val result = falseIfAnyConditionIsFalse()
        shownIf(result)
        if (!result) property.value = null
    }
    conditions(dependency)
    dependency.evaluate()
}

fun <View : android.view.View> View.depends(conditions: (CSPropertyConditionList).() -> Unit) =
    apply {
        val dependency = CSPropertyConditionList { shownIf(falseIfAnyConditionIsFalse()) }
        conditions(dependency)
        dependency.evaluate()
    }

class CSPropertyConditionList(val evaluate: (CSPropertyConditionList).() -> Unit) {
    val conditions = mutableListOf<CSPropertyCondition<*>>()

    fun <T> on(property: CSEventProperty<T?>, condition: (T?) -> Boolean?) {
        conditions.add(CSPropertyCondition(property, condition))
        navigation.register(property.onChange { evaluate() })
    }

    fun evaluate() = evaluate(this)
}

private fun CSPropertyConditionList.falseIfAnyConditionIsFalse(): Boolean {
    var result = true
    conditions.forEach { if (!it.evaluate().isTrue) result = false }
    return result
}

class CSPropertyCondition<T>(val property: CSEventProperty<T?>, val condition: (T?) -> Boolean?) {
    fun evaluate() = condition(property.value)
}