package renetik.android.controller.extensions

import android.view.View
import android.widget.RadioGroup
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import renetik.android.controller.common.CSNavigationInstance.navigation
import renetik.android.java.event.CSEventProperty
import renetik.android.java.extensions.asString
import renetik.android.java.extensions.isSet
import renetik.android.material.extensions.clearError
import renetik.android.material.extensions.onClear
import renetik.android.material.extensions.onTextChange
import renetik.android.view.extensions.onChange
import renetik.android.view.extensions.onTextChange
import renetik.android.view.extensions.shownIf
import renetik.android.view.extensions.title
import renetik.android.view.extensions.text

fun <T : Any> TextInputLayout.property(property: CSEventProperty<T?>,
                                       depends: ((CSPropertyConditionList).() -> Unit)? = null) =
    apply {
        onTextChange { if (property.value.isSet) clearError() }
        onClear { property.value = null }
        editText!!.property(property)
        if (depends != null) depends(property, depends)
    }

@JvmName("propertyString")
fun TextInputLayout.property(property: CSEventProperty<String?>,
                             depends: ((CSPropertyConditionList).() -> Unit)? = null) = apply {
    onTextChange { if (property.value.isSet) clearError() }
    onClear { property.value = null }
    editText!!.property(property)
    if (depends != null) depends(property, depends)
}

fun <T : Any> TextView.property(property: CSEventProperty<T?>,
                                depends: ((CSPropertyConditionList).() -> Unit)? = null) = apply {
    fun updateTitle() = text(property.value.asString())
    navigation.register(property.onChange { updateTitle() })
    updateTitle()
    if (depends != null) depends(property, depends)
}

@JvmName("propertyString")
fun TextView.property(property: CSEventProperty<String?>,
                      depends: ((CSPropertyConditionList).() -> Unit)? = null) = apply {
    fun updateTitle() = text(property.value.asString())
    val onPropertyChange = navigation.register(property.onChange { updateTitle() })
    onTextChange {
        onPropertyChange?.isActive = false
        property.value = if (it.title.isSet) it.title else null
        onPropertyChange?.isActive = true
    }
    updateTitle()
    if (depends != null) depends(property, depends)
}

fun <T> RadioGroup.property(property: CSEventProperty<T?>, mapOf: Map<Int, T>) =
    onChange { property.value = mapOf[it] }

fun View.shownIfValueSet(property: CSEventProperty<*>) = apply {
    fun updateVisibility() = shownIf(property.value.isSet)
    navigation.register(property.onChange { updateVisibility() })
    updateVisibility()
}

fun <T> View.shownIfValueEquals(property: CSEventProperty<T?>, value: T) = apply {
    fun updateVisibility() = shownIf(property.value == value)
    navigation.register(property.onChange { shownIf(property.value == value) })
    updateVisibility()
}

fun RadioGroup.propertyTrueIf(property: CSEventProperty<Boolean?>, viewId: Int) = apply {
    onChange { property.value = it == viewId }
}