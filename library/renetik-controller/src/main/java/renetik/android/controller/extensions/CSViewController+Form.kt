package renetik.android.controller.extensions

import android.view.View
import android.widget.RadioGroup
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import renetik.android.controller.common.CSNavigationInstance.navigation
import renetik.android.java.common.CSName
import renetik.android.java.event.CSEventProperty
import renetik.android.java.extensions.asString
import renetik.android.java.extensions.isSet
import renetik.android.java.extensions.primitives.isTrue
import renetik.android.material.extensions.clearError
import renetik.android.material.extensions.onClear
import renetik.android.material.extensions.onTextChange
import renetik.android.view.extensions.onChange
import renetik.android.view.extensions.onTextChange
import renetik.android.view.extensions.shownIf
import renetik.android.view.extensions.title

fun <T : Any> TextInputLayout.property(property: CSEventProperty<T?>,
                                       depends: ((CSFormFieldDependency).() -> Unit)? = null) =
    apply {
        onTextChange { if (property.value.isSet) clearError() }
        onClear { property.value = null }
        editText!!.property(property)
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

fun <T> RadioGroup.property(property: CSEventProperty<T?>, mapOf: Map<Int, T>) =
    onChange { property.value = mapOf[it] }

@JvmName("propertyString")
fun TextInputLayout.property(property: CSEventProperty<String?>) = apply {
    onTextChange { if (property.value.isSet) clearError() }
    onClear { property.value = null }
    editText!!.property(property)
}

fun TextView.property(property: CSEventProperty<*>) = apply {
    fun updateTitle() = title(property.value.asString())
    navigation.register(property.onChange { updateTitle() })
    updateTitle()
}

@JvmName("propertyString")
fun TextView.property(property: CSEventProperty<String?>) = apply {
    fun updateTitle() = title(property.value.asString())
    val onPropertyChange = navigation.register(property.onChange { updateTitle() })
    onTextChange {
        onPropertyChange?.isActive = false
        property.value = if (it.title.isSet) it.title else null
        onPropertyChange?.isActive = true
    }
    updateTitle()
}

fun View.shownIfSet(property: CSEventProperty<*>) = apply {
    fun updateVisibility() = shownIf(property.value.isSet)
    navigation.register(property.onChange { updateVisibility() })
    updateVisibility()
}

//fun <T : Enum<T>> View.shownIf(property: CSEventProperty<T?>, value: T) = apply {
//    fun updateVisibility() = shownIf(property.value == value)
//    navigation.register(property.onChange { shownIf(property.value == value) })
//    updateVisibility() //TODO good for something ????
//}

fun <T> View.shownIf(property: CSEventProperty<T?>, value: T) = apply {
    fun updateVisibility() = shownIf(property.value == value)
    navigation.register(property.onChange { shownIf(property.value == value) })
    updateVisibility()
}

fun RadioGroup.propertyTrueIf(property: CSEventProperty<Boolean?>, viewId: Int) = apply {
    onChange { property.value = it == viewId }
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