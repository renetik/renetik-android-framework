package renetik.android.controller.extensions

import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.google.android.material.textfield.TextInputLayout
import renetik.android.controller.common.CSNavigationInstance.navigation
import renetik.android.framework.lang.CSName
import renetik.android.java.event.CSEventProperty
import renetik.android.java.extensions.asString
import renetik.android.java.extensions.isSet
import renetik.android.material.extensions.errorClear
import renetik.android.material.extensions.onClear
import renetik.android.material.extensions.onTextChange
import renetik.android.view.extensions.*

private fun <View : android.view.View, T : Any> View.depends(
    property: CSEventProperty<T?>, conditions: (CSPropertyConditionList).() -> Unit,
    isInContainer: Boolean) = apply {
    validate(conditions) { result ->
        if (isInContainer) superview!!.shownIf(result)
        else shownIf(result)
        if (!result) property.value = null
    }
}

fun <T : Any> TextInputLayout.property(property: CSEventProperty<T?>,
                                       depends: ((CSPropertyConditionList).() -> Unit)? = null,
                                       isInContainer: Boolean = false) =
    apply {
        onTextChange { if (property.value.isSet) errorClear() }
        onClear { property.value = null }
        editText!!.property(property)
        if (depends != null) depends(property, depends, isInContainer)
    }

@JvmName("propertyString")
fun TextInputLayout.property(property: CSEventProperty<String?>,
                             depends: ((CSPropertyConditionList).() -> Unit)? = null,
                             isInContainer: Boolean = false) = apply {
    onTextChange { if (property.value.isSet) errorClear() }
    onClear { property.value = null }
    editText!!.property(property)
    if (depends != null) depends(property, depends, isInContainer)
}

fun <T : Any> TextView.property(property: CSEventProperty<T?>,
                                depends: ((CSPropertyConditionList).() -> Unit)? = null,
                                isInContainer: Boolean = false) = apply {
    fun updateTitle() = text(property.value.asString)
    navigation.register(property.onChange { updateTitle() })
    updateTitle()
    if (depends != null) depends(property, depends, isInContainer)
}


@JvmName("propertyString")
fun TextView.property(property: CSEventProperty<String?>,
                      depends: ((CSPropertyConditionList).() -> Unit)? = null,
                      isInContainer: Boolean = false) = apply {
    fun updateTitle() = text(property.value.asString)
    val onPropertyChange = navigation.register(property.onChange { updateTitle() })
    onTextChange {
        onPropertyChange?.isActive = false
        property.value = if (it.title.isSet) it.title else null
        onPropertyChange?.isActive = true
    }
    updateTitle()
    if (depends != null) depends(property, depends, isInContainer)
}

fun <T : CSName> RadioGroup.property(
    property: CSEventProperty<T?>, list: List<T>, @LayoutRes layoutId: Int,
    depends: ((CSPropertyConditionList).() -> Unit)? = null,
    isInContainer: Boolean = false) = apply {
    val data = mutableMapOf<Int, T>()
    removeAllViews()
    list.forEach {
        val viewId = View.generateViewId()
        add(inflate<RadioButton>(layoutId)).text(it.name).model(it).id(viewId)
        data[viewId] = it
    }
    property(property, data, depends, isInContainer)

//    fun updateChecked() {
//        checkedId = childAt { it.model<T>() == property.value }?.id
//    }
//
//    val onPropertyChange = navigation.register(property.onChange { updateChecked() })
//    onChange {
//        onPropertyChange?.isActive = false
//        property.value = radio(it).model()
//        onPropertyChange?.isActive = true
//    }
//    updateChecked()
//    if (depends != null) depends(property, depends, isInContainer)
}

fun <T : Any> RadioGroup.property(
    property: CSEventProperty<T?>, data: Map<Int, T>,
    depends: ((CSPropertyConditionList).() -> Unit)? = null,
    isInContainer: Boolean = false) = apply {
    fun updateChecked() {
        checkedId = data.filterValues { it == property.value }.keys.firstOrNull()
    }

    val onPropertyChange = navigation.register(property.onChange { updateChecked() })
    onChange {
        onPropertyChange?.isActive = false
        property.value = data[it]
        onPropertyChange?.isActive = true
    }
    updateChecked()
    if (depends != null) depends(property, depends, isInContainer)
}

fun View.shownIfPropertySet(property: CSEventProperty<*>) = apply {
    fun updateVisibility() = shownIf(property.value.isSet)
    navigation.register(property.onChange { updateVisibility() })
    updateVisibility()
}

fun View.shownIfProperty(property: CSEventProperty<Boolean>) = apply {
    fun updateVisibility() = shownIf(property.value)
    navigation.register(property.onChange { updateVisibility() })
    updateVisibility()
}

fun <T> View.shownIfProperty(property: CSEventProperty<T?>, value: T) = apply {
    fun updateVisibility() = shownIf(property.value == value)
    navigation.register(property.onChange { updateVisibility() })
    updateVisibility()
}

fun RadioGroup.propertyTrueIf(property: CSEventProperty<Boolean?>, viewId: Int) = apply {
    onChange { property.value = it == viewId }
}