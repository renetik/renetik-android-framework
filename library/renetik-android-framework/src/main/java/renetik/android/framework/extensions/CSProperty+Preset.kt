package renetik.android.framework.extensions

import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import renetik.android.controller.base.CSView
import renetik.android.core.kotlin.collections.put
import renetik.android.core.kotlin.primitives.update
import renetik.android.event.common.onDestructed
import renetik.android.event.registration.CSHasChange
import renetik.android.event.registration.CSHasChangeValue
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.plus
import renetik.android.preset.Preset
import renetik.android.preset.extensions.action
import renetik.android.ui.extensions.add
import renetik.android.ui.extensions.minusAssign
import renetik.android.ui.protocol.CSViewInterface

fun <ItemView : CSViewInterface> CSHasChangeValue<Int>.updates(
    preset: Preset,
    items: MutableList<ItemView>,
    layout: ViewGroup, fromStart: Boolean = false,
    layoutParams: LayoutParams? = null,
    createView: (index: Int) -> ItemView
): CSRegistration = action(preset) { value ->
    items.size.update(value,
        onAdd = { index ->
            val view = items.put(createView(index))
            val addIndex = if (fromStart) 0 else -1
            layoutParams?.let { layout.add(view = view, params = it, index = addIndex) }
                ?: layout.add(view = view, index = addIndex)
        },
        onRemove = { index ->
            val itemView: ItemView = items.removeAt(index)
            if (!itemView.isDestructed) layout -= itemView
        })
}

fun <View : CSViewInterface, Model> MutableList<View>.viewFactory(
    parent: CSView<*>, list: List<Model>, eventAdded: CSHasChange<Model>,
    content: ViewGroup, layoutParams: LayoutParams? = null,
    fromStart: Boolean = false, create: (Model) -> View
) = apply {
    fun createView(model: Model) {
        this += create(model).also { view ->
            val addIndex = if (fromStart) 0 else -1
            layoutParams?.let { content.add(view = view, params = it, addIndex) }
                ?: content.add(view = view, addIndex)
            view.onDestructed { this -= view; content -= view }
        }
    }
    list.forEach(::createView)
    parent + eventAdded.onChange(::createView)
}