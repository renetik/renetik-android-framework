package renetik.android.widget

import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import renetik.android.R
import renetik.android.framework.event.CSVisibleEventOwner
import renetik.android.framework.event.event
import renetik.android.framework.event.listen
import renetik.android.framework.event.pause
import renetik.android.framework.event.property.CSEventProperty
import renetik.android.framework.lang.CSTitle
import renetik.android.view.*

fun View.radioGroup(@IdRes id: Int) = findView<RadioGroup>(id)!!

val <T : RadioGroup> T.eventChange
    get() = propertyWithTag(R.id.ViewEventOnScrollChangeTag) { event<Int>() }

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

fun <T : CSTitle> RadioGroup.property(
    parent: CSVisibleEventOwner, property: CSEventProperty<T?>,
    list: List<T>, @LayoutRes layoutId: Int) = apply {
    val data = mutableMapOf<Int, T>()
    clear()
    list.forEach {
        val viewId = View.generateViewId()
        add(inflate<RadioButton>(layoutId)).text(it.title).model(it).id(viewId)
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
    onChange { buttonId -> onPropertyChange.pause().use { property.value = data[buttonId] } }
    updateChecked()
}