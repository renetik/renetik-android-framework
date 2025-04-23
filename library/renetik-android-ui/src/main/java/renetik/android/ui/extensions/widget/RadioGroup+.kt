package renetik.android.ui.extensions.widget

import android.widget.RadioButton
import android.widget.RadioGroup
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.property.CSProperty
import renetik.android.ui.R
import renetik.android.ui.extensions.view.propertyWithTag
import renetik.android.ui.extensions.view.view

val <T : RadioGroup> T.eventChange
    get() = propertyWithTag(R.id.ViewEventOnScrollTag) { event<Int>() }

fun RadioGroup.onChange(listener: (buttonId: Int) -> Unit) = apply {
    eventChange.listen(listener)
    setOnCheckedChangeListener { _, checkedId -> eventChange.fire(checkedId) }
}

var RadioGroup.checkedId: Int?
    get() = if (checkedRadioButtonId != -1) checkedRadioButtonId else null
    set(value) = if (value == null) check(-1) else check(value)

val RadioGroup.checkedText: String
    get() = checkedId?.let { view<RadioButton>(it).text() } ?: ""

val RadioGroup.isChecked get() = checkedId != null

fun RadioGroup.propertyTrueIfChecked(property: CSProperty<Boolean?>, viewId: Int) = apply {
    onChange { property.value = it == viewId }
}

//fun <T : CSHasTitle> RadioGroup.property(
//    parent: CSVisibleHasRegistrations, property: CSProperty<T?>,
//    list: List<T>, @LayoutRes layoutId: Int) = apply {
//    val data = mutableMapOf<Int, T>()
//    clear()
//    list.forEach {
//        val viewId = View.generateViewId()
//        add(inflate<RadioButton>(layoutId)).text(it.title).model(it).id(viewId)
//        data[viewId] = it
//    }
//    property(parent, property, data)
//}
//
//fun <T : Any> RadioGroup.property(
//    parent: CSVisibleHasRegistrations, property: CSProperty<T?>, data: Map<Int, T>) = apply {
//    fun updateChecked() {
//        checkedId = data.filterValues { it == property.value }.keys.firstOrNull()
//    }
//
//    val onPropertyChange = parent.whileShowing(property.onChange { updateChecked() })
//    onChange { buttonId -> onPropertyChange.paused { property.value = data[buttonId] } }
//    updateChecked()
//}