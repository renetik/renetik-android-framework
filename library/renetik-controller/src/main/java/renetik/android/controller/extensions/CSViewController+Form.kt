package renetik.android.controller.extensions

import com.google.android.material.textfield.TextInputLayout
import renetik.android.controller.common.CSNavigationInstance.navigation
import renetik.android.java.common.CSName
import renetik.android.java.event.CSEventProperty
import renetik.android.java.extensions.isSet
import renetik.android.java.extensions.primitives.isTrue
import renetik.android.java.extensions.stringify
import renetik.android.material.extensions.clearError
import renetik.android.material.extensions.onClear
import renetik.android.material.extensions.onTextChange
import renetik.android.material.extensions.title
import renetik.android.view.extensions.onClear
import renetik.android.view.extensions.onTextChange
import renetik.android.view.extensions.shownIf

fun <T : Any> TextInputLayout.data(property: CSEventProperty<T?>,
                                   depends: ((CSFormFieldDependency).() -> Unit)? = null) = apply {
    onTextChange { if (property.value.isSet) clearError() }
    onClear { property.value = null }
    fun updateTitle() = title(property.value?.stringify() ?: "")
    updateTitle()
    navigation.register(property.onChange { updateTitle() })
    if (depends != null) {
        val dependency = CSFormFieldDependency {
            val result = falseIfAnyConditionIsFalse()
            shownIf(result)
            if (!result) property.value = null
        }
        depends(dependency)
        dependency.evaluate()
    }
}

fun <T : Any> TextInputLayout.data(property: CSEventProperty<T>) = apply {
    fun updateTitle() = title(property.value.stringify())
    updateTitle()
    navigation.register(property.onChange { updateTitle() })
}

fun <View : android.view.View> View.depends(depend: (CSFormFieldDependency).() -> Unit) = apply {
    CSFormFieldDependency { shownIf(falseIfAnyConditionIsFalse()) }.also { depend(it) }.evaluate()
}

class CSFormFieldDependency(val evaluate: (CSFormFieldDependency).() -> Unit) {
    val conditions = mutableListOf<CSDependCondition<*>>()

    fun <T : CSName> on(property: CSEventProperty<T?>, condition: (T?) -> Boolean?) {
        conditions.add(CSDependCondition(property, condition))
        navigation.register(property.onChange { evaluate() })
    }

    fun evaluate() = evaluate(this)
}

private fun CSFormFieldDependency.falseIfAnyConditionIsFalse(): Boolean {
    var result = true
    conditions.forEach { if (!it.evaluate().isTrue) result = false }
    return result
}

class CSDependCondition<T>(val property: CSEventProperty<T?>, val condition: (T?) -> Boolean?) {
    fun evaluate() = condition(property.value)
}