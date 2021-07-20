package renetik.android.view.extensions

import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import renetik.android.R
import renetik.android.framework.lang.CSName
import renetik.android.java.event.*
import renetik.android.java.event.property.CSEventProperty

fun View.radioGroup(@IdRes id: Int) = findView<RadioGroup>(id)!!

val <T : RadioGroup> T.eventChange
    get() = propertyWithTag(R.id.ViewEventOnChangeTag) { event<Int>() }

fun RadioGroup.onChange(listener: (buttonId: Int) -> Unit) = apply {
    eventChange.listen(listener)
    setOnCheckedChangeListener { _, checkedId -> eventChange.fire(checkedId) }
}

var RadioGroup.checkedId: Int?
    get() = if (checkedRadioButtonId != -1) checkedRadioButtonId else null
    set(value) = if (value == null) check(-1) else check(value)

val RadioGroup.isChecked get() = checkedId != null

fun RadioGroup.propertyTrueIfChecked(property: CSEventProperty<Boolean?>, viewId: Int) = apply {
    onChange { property.value = it == viewId }
}

fun <T : CSName> RadioGroup.property(
    parent: CSVisibleEventOwner, property: CSEventProperty<T?>,
    list: List<T>, @LayoutRes layoutId: Int) = apply {
    val data = mutableMapOf<Int, T>()
    removeAllViews()
    list.forEach {
        val viewId = View.generateViewId()
        add(inflate<RadioButton>(layoutId)).text(it.name).model(it).id(viewId)
        data[viewId] = it
    }
    property(parent, property, data)
}

fun <T : Any> RadioGroup.property(
    parent: CSVisibleEventOwner, property: CSEventProperty<T?>, data: Map<Int, T>) = apply {
    fun updateChecked() {
        checkedId = data.filterValues { it == property.value }.keys.firstOrNull()
    }

    val onPropertyChange = parent.whileShowing(property.onChange { updateChecked() })
    onChange {
        onPropertyChange.pause()
        property.value = data[it]
        onPropertyChange.resume()
    }
    updateChecked()
}